package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.service.SecurityService
import com.finanzas.dash.finanzas.repository.MonthlyStatementRepository
import com.finanzas.dash.finanzas.dto.response.statement.StatementResponseDto
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/api/statement")
class StatementController(
    private val securityService: SecurityService,
    private val monthlyStatementRepository: MonthlyStatementRepository
) {

    @GetMapping("/user")
    fun getUserStatements(): ResponseEntity<List<StatementResponseDto>> {
        try {
            val user = securityService.currentUser()
            val statements = monthlyStatementRepository.findAllByUser_UserIdOrderByYearDescMonthDesc(user.userId!!)
            
            val dtos = statements.map { 
                StatementResponseDto(
                    id = it.id,
                    month = it.month,
                    year = it.year,
                    downloadUrl = "/api/statement/download?month=${it.month}&year=${it.year}"
                )
            }
            return ResponseEntity.ok(dtos)
        } catch (e: Exception) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/download")
    fun downloadStatement(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): ResponseEntity<Resource> { // Return Resource for file download
        try {
            val user = securityService.currentUser()
            val fileName = "statement_${user.userId}_${month}_${year}.pdf"
            
            // Directorio interno del proyecto
            val filePath = "statements/$fileName"
            val file = File(filePath)

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
