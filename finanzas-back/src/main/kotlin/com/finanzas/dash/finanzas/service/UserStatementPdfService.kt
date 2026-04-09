package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.User
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class StockStatementRow(
    val stockName: String,
    val totalInvested: BigDecimal,
    val marketValue: BigDecimal,
    val capitalGains: BigDecimal,
    val fees: BigDecimal,
    val taxes: BigDecimal,
    val dividends: BigDecimal
)

data class FixedStatementRow(
    val instrumentName: String,
    val investedAmount: BigDecimal,
    val monthlyInterestGenerated: BigDecimal
)

@Service
class UserStatementPdfService(
    private val templateEngine: TemplateEngine
) {

    /**
     * Renders the shared Thymeleaf "statement" template to an HTML string.
     * This HTML is reused for both PDF generation and email body.
     */
    fun renderStatementHtml(
        user: User,
        month: Int,
        year: Int,
        stockRows: List<StockStatementRow>,
        fixedRows: List<FixedStatementRow>
    ): String {
        var totalCapitalGains = BigDecimal.ZERO
        var totalFees = BigDecimal.ZERO
        var totalTaxes = BigDecimal.ZERO
        var totalDividends = BigDecimal.ZERO

        for (row in stockRows) {
            totalCapitalGains += row.capitalGains
            totalFees += row.fees
            totalTaxes += row.taxes
            totalDividends += row.dividends
        }

        var totalFixedInterest = BigDecimal.ZERO
        for (row in fixedRows) {
            totalFixedInterest += row.monthlyInterestGenerated
        }

        val grandTotal = totalCapitalGains + totalFixedInterest + totalDividends - totalFees - totalTaxes

        val ym = YearMonth.of(year, month)
        val monthName = ym.month.getDisplayName(TextStyle.FULL, Locale.of("es", "MX"))
            .replaceFirstChar { it.uppercaseChar() }
        val periodLabel = "$monthName $year"

        val ctx = Context()
        ctx.setVariable("username", user.username ?: "N/A")
        ctx.setVariable("periodLabel", periodLabel)
        ctx.setVariable("stockRows", stockRows)
        ctx.setVariable("fixedRows", fixedRows)
        ctx.setVariable("totalFees", totalFees.setScale(2, RoundingMode.HALF_UP))
        ctx.setVariable("totalTaxes", totalTaxes.setScale(2, RoundingMode.HALF_UP))
        ctx.setVariable("totalDividends", totalDividends.setScale(2, RoundingMode.HALF_UP))
        ctx.setVariable("totalCapitalGains", totalCapitalGains.setScale(2, RoundingMode.HALF_UP))
        ctx.setVariable("totalFixedInterest", totalFixedInterest.setScale(2, RoundingMode.HALF_UP))
        ctx.setVariable("grandTotal", grandTotal.setScale(2, RoundingMode.HALF_UP))

        return templateEngine.process("statement", ctx)
    }

    /**
     * Generates a PDF file from the statement HTML and returns the file name.
     */
    fun generateMonthlyStatement(
        user: User,
        month: Int,
        year: Int,
        stockRows: List<StockStatementRow>,
        fixedRows: List<FixedStatementRow>,
        directoryPath: String
    ): String {
        val fileName = "statement_${user.userId}_${month}_${year}.pdf"
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val filePath = "$directoryPath/$fileName"
        val html = renderStatementHtml(user, month, year, stockRows, fixedRows)

        FileOutputStream(filePath).use { os ->
            PdfRendererBuilder()
                .useFastMode()
                .withHtmlContent(html, "/")
                .toStream(os)
                .run()
        }

        return fileName
    }
}
