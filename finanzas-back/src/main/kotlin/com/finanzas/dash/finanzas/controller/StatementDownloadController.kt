package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.service.SecurityService
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.File

@Controller
@RequestMapping("/api/statement/web")
class StatementDownloadController(
    private val securityService: SecurityService
) {

    @GetMapping("/download")
    fun downloadStatementWeb(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): ResponseEntity<*> {
        try {
            val user = securityService.currentUser()
            val fileName = "statement_${user.userId}_${month}_${year}.pdf"
            
            val filePath = "statements/$fileName"
            val file = File(filePath)

            if (!file.exists()) {
                // Return an HTML page so the browser doesn't show a generic 404 error
                val html = """
                    <html>
                    <head><title>Estado de Cuenta no Encontrado</title></head>
                    <body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
                        <h2 style="color: #d9534f;">Archivo no encontrado</h2>
                        <p>El estado de cuenta solicitado ($month/$year) aún no se ha generado o el archivo f&iacute;sico no existe en el servidor.</p>
                        <button onclick="window.close()" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">Cerrar esta pesta&ntilde;a</button>
                    </body>
                    </html>
                """.trimIndent()
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html)
            }

            val resource = FileSystemResource(file)
            val headers = HttpHeaders()
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Estado_De_Cuenta_${month}_${year}.pdf\"")

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource)

        } catch (e: Exception) {
            val html = """
                 <html>
                 <head><title>Error</title></head>
                 <body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
                     <h2 style="color: #d9534f;">Error Interno</h2>
                     <p>Ocurrió un error al procesar tu solicitud.</p>
                 </body>
                 </html>
             """.trimIndent()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body(html)
        }
    }
}
