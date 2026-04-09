package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.User
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.File
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val statementPdfService: UserStatementPdfService
) {
    private val log = LoggerFactory.getLogger(EmailService::class.java)

    @Value("\${mail.from:noreply@finanzas.com}")
    private lateinit var mailFrom: String

    @Value("\${mail.enabled:false}")
    private var mailEnabled: Boolean = false

    companion object {
        // Standard email regex — RFC 5322 simplified
        private val EMAIL_REGEX = Regex(
            "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$"
        )
    }

    /**
     * Returns true if the given string looks like a valid email address.
     */
    fun isValidEmail(value: String): Boolean {
        return value.isNotBlank() && EMAIL_REGEX.matches(value.trim())
    }

    /**
     * Sends the monthly statement email with the PDF attached.
     * Does nothing if mail is disabled or the username is not a valid email.
     */
    fun sendStatementEmail(
        user: User,
        month: Int,
        year: Int,
        pdfFilePath: String,
        stockRows: List<StockStatementRow>,
        fixedRows: List<FixedStatementRow>
    ) {
        if (!mailEnabled) {
            log.debug("Envío de correo deshabilitado (mail.enabled=false). Omitiendo envío para usuario ${user.userId}")
            return
        }

        val recipientEmail = user.username?.trim() ?: return
        if (!isValidEmail(recipientEmail)) {
            log.debug("Username '${recipientEmail}' no es un email válido. Omitiendo envío.")
            return
        }

        try {
            val ym = YearMonth.of(year, month)
            val monthName = ym.month.getDisplayName(TextStyle.FULL, Locale.of("es", "MX"))
                .replaceFirstChar { it.uppercaseChar() }
            val subject = "Estado de Cuenta - $monthName $year"

            // Render the same HTML template used for the PDF
            val htmlBody = statementPdfService.renderStatementHtml(user, month, year, stockRows, fixedRows)

            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setFrom(mailFrom)
            helper.setTo(recipientEmail)
            helper.setSubject(subject)
            helper.setText(htmlBody, true) // true = isHtml

            // Attach PDF
            val pdfFile = File(pdfFilePath)
            if (pdfFile.exists()) {
                val resource = FileSystemResource(pdfFile)
                helper.addAttachment("Estado_de_Cuenta_${monthName}_${year}.pdf", resource)
            }

            mailSender.send(message)
            log.info("Email de estado de cuenta enviado a: $recipientEmail (usuario ${user.userId})")
        } catch (e: Exception) {
            log.error("Error al enviar email de estado de cuenta al usuario ${user.userId}: ${e.message}", e)
        }
    }
}
