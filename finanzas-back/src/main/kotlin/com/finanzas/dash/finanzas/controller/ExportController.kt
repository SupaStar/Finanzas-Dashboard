package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.service.ExportService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/export")
class ExportController(private val exportService: ExportService) {

    /**
     * Export operations of a single portfolio as XLSX
     * GET /export/operations/{portfolioId}
     */
    @GetMapping("/operations/{portfolioId}")
    fun exportOperations(@PathVariable portfolioId: Long): ResponseEntity<ByteArray> {
        val bytes = exportService.exportOperations(portfolioId)
        return buildXlsxResponse(bytes, "operaciones_portafolio_$portfolioId.xlsx")
    }

    /**
     * Export dividends of a single portfolio as XLSX
     * GET /export/dividends/{portfolioId}
     */
    @GetMapping("/dividends/{portfolioId}")
    fun exportDividends(@PathVariable portfolioId: Long): ResponseEntity<ByteArray> {
        val bytes = exportService.exportDividends(portfolioId)
        return buildXlsxResponse(bytes, "dividendos_portafolio_$portfolioId.xlsx")
    }

    /**
     * Export all operations and dividends of all user portfolios as XLSX
     * GET /export/all
     */
    @GetMapping("/all")
    fun exportAll(): ResponseEntity<ByteArray> {
        val bytes = exportService.exportAll()
        return buildXlsxResponse(bytes, "exportacion_completa.xlsx")
    }

    private fun buildXlsxResponse(bytes: ByteArray, filename: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(bytes.size.toLong())
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(bytes)
    }
}
