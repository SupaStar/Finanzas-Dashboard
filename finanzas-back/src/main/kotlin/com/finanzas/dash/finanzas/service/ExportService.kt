package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.entity.Dividend
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.repository.DividendRepository
import com.finanzas.dash.finanzas.repository.OperationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Service
class ExportService(
    private val portfolioRepository: PortfolioRepository,
    private val operationRepository: OperationRepository,
    private val dividendRepository: DividendRepository,
    private val securityService: SecurityService
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Export operations for a single portfolio as XLSX
     */
    fun exportOperations(portfolioId: Long): ByteArray {
        val user = securityService.currentUser()
        val portfolio = portfolioRepository.findByPortfolioId(portfolioId)
            ?: throw GeneralRequestException(listOf("Portafolio no encontrado"), HttpStatus.NOT_FOUND)

        if (portfolio.user?.userId != user.userId) {
            throw GeneralRequestException(listOf("No tienes permisos para exportar este portafolio"), HttpStatus.FORBIDDEN)
        }

        val operations = operationRepository.findByPortfolioPortfolioId(portfolioId)
            .sortedByDescending { it.operationDate }

        val workbook = XSSFWorkbook()
        val stockSymbol = portfolio.stock?.symbol ?: "Desconocido"
        writeOperationsSheet(workbook, "Operaciones - $stockSymbol", operations, portfolio)
        return workbookToBytes(workbook)
    }

    /**
     * Export dividends for a single portfolio as XLSX
     */
    fun exportDividends(portfolioId: Long): ByteArray {
        val user = securityService.currentUser()
        val portfolio = portfolioRepository.findByPortfolioId(portfolioId)
            ?: throw GeneralRequestException(listOf("Portafolio no encontrado"), HttpStatus.NOT_FOUND)

        if (portfolio.user?.userId != user.userId) {
            throw GeneralRequestException(listOf("No tienes permisos para exportar este portafolio"), HttpStatus.FORBIDDEN)
        }

        val dividends = dividendRepository.findByPortfolioPortfolioId(portfolioId)
            .sortedByDescending { it.paidDate }

        val workbook = XSSFWorkbook()
        val stockSymbol = portfolio.stock?.symbol ?: "Desconocido"
        writeDividendsSheet(workbook, "Dividendos - $stockSymbol", dividends, portfolio)
        return workbookToBytes(workbook)
    }

    /**
     * Export all operations and dividends for all user portfolios as XLSX (one file, multiple sheets)
     */
    fun exportAll(): ByteArray {
        val user = securityService.currentUser()
        val portfolios = portfolioRepository.findByUserUserId(user.userId!!)

        val workbook = XSSFWorkbook()

        // Sheet for all operations
        val allOperations = mutableListOf<Pair<Portfolio, Operation>>()
        val allDividends = mutableListOf<Pair<Portfolio, Dividend>>()

        portfolios.forEach { portfolio ->
            val operations = operationRepository.findByPortfolioPortfolioId(portfolio.portfolioId!!)
            operations.forEach { allOperations.add(portfolio to it) }

            val dividends = dividendRepository.findByPortfolioPortfolioId(portfolio.portfolioId!!)
            dividends.forEach { allDividends.add(portfolio to it) }
        }

        writeAllOperationsSheet(workbook, "Operaciones", allOperations.sortedByDescending { it.second.operationDate })
        writeAllDividendsSheet(workbook, "Dividendos", allDividends.sortedByDescending { it.second.paidDate })

        return workbookToBytes(workbook)
    }

    // ─── Sheet Writers ───────────────────────────────────────────────

    private fun writeOperationsSheet(
        workbook: XSSFWorkbook,
        sheetName: String,
        operations: List<Operation>,
        portfolio: Portfolio
    ) {
        val sheet = workbook.createSheet(sheetName)
        val headerStyle = createHeaderStyle(workbook)
        val headers = listOf("Tipo", "Cantidad", "Precio", "Impuesto", "Comisión", "Neto", "Fecha", "Símbolo", "Moneda")

        // Header row
        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { idx, title ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }

        val stockSymbol = portfolio.stock?.symbol ?: ""

        // Data rows
        operations.forEachIndexed { idx, op ->
            val row = sheet.createRow(idx + 1)
            row.createCell(0).setCellValue(op.operationType?.name ?: "")
            row.createCell(1).setCellValue(op.quantity?.toDouble() ?: 0.0)
            row.createCell(2).setCellValue(op.price?.toDouble() ?: 0.0)
            row.createCell(3).setCellValue(op.tax.toDouble())
            row.createCell(4).setCellValue(op.fee.toDouble())
            row.createCell(5).setCellValue(op.total?.toDouble() ?: 0.0)
            row.createCell(6).setCellValue(op.operationDate?.format(dateFormatter) ?: "")
            row.createCell(7).setCellValue(stockSymbol)
            row.createCell(8).setCellValue(portfolio.stock?.currency ?: "")
        }

        // Auto-size columns
        headers.indices.forEach { sheet.autoSizeColumn(it) }
    }

    private fun writeDividendsSheet(
        workbook: XSSFWorkbook,
        sheetName: String,
        dividends: List<Dividend>,
        portfolio: Portfolio
    ) {
        val sheet = workbook.createSheet(sheetName)
        val headerStyle = createHeaderStyle(workbook)
        val headers = listOf("Monto", "Fecha", "Impuesto", "Neto", "Símbolo")

        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { idx, title ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }

        val stockSymbol = portfolio.stock?.symbol ?: ""

        dividends.forEachIndexed { idx, div ->
            val row = sheet.createRow(idx + 1)
            row.createCell(0).setCellValue(div.value?.toDouble() ?: 0.0)
            row.createCell(1).setCellValue(div.paidDate?.format(dateFormatter) ?: "")
            row.createCell(2).setCellValue(div.tax.toDouble())
            row.createCell(3).setCellValue(div.netValue?.toDouble() ?: 0.0)
            row.createCell(4).setCellValue(stockSymbol)
        }

        headers.indices.forEach { sheet.autoSizeColumn(it) }
    }

    private fun writeAllOperationsSheet(
        workbook: XSSFWorkbook,
        sheetName: String,
        operations: List<Pair<Portfolio, Operation>>
    ) {
        val sheet = workbook.createSheet(sheetName)
        val headerStyle = createHeaderStyle(workbook)
        val headers = listOf("Tipo", "Cantidad", "Precio", "Impuesto", "Comisión", "Neto", "Fecha", "Símbolo", "Moneda")

        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { idx, title ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }

        operations.forEachIndexed { idx, (portfolio, op) ->
            val row = sheet.createRow(idx + 1)
            row.createCell(0).setCellValue(op.operationType?.name ?: "")
            row.createCell(1).setCellValue(op.quantity?.toDouble() ?: 0.0)
            row.createCell(2).setCellValue(op.price?.toDouble() ?: 0.0)
            row.createCell(3).setCellValue(op.tax.toDouble())
            row.createCell(4).setCellValue(op.fee.toDouble())
            row.createCell(5).setCellValue(op.total?.toDouble() ?: 0.0)
            row.createCell(6).setCellValue(op.operationDate?.format(dateFormatter) ?: "")
            row.createCell(7).setCellValue(portfolio.stock?.symbol ?: "")
            row.createCell(8).setCellValue(portfolio.stock?.currency ?: "")
        }

        headers.indices.forEach { sheet.autoSizeColumn(it) }
    }

    private fun writeAllDividendsSheet(
        workbook: XSSFWorkbook,
        sheetName: String,
        dividends: List<Pair<Portfolio, Dividend>>
    ) {
        val sheet = workbook.createSheet(sheetName)
        val headerStyle = createHeaderStyle(workbook)
        val headers = listOf("Monto", "Fecha", "Impuesto", "Neto", "Símbolo")

        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { idx, title ->
            val cell = headerRow.createCell(idx)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }

        dividends.forEachIndexed { idx, (portfolio, div) ->
            val row = sheet.createRow(idx + 1)
            row.createCell(0).setCellValue(div.value?.toDouble() ?: 0.0)
            row.createCell(1).setCellValue(div.paidDate?.format(dateFormatter) ?: "")
            row.createCell(2).setCellValue(div.tax.toDouble())
            row.createCell(3).setCellValue(div.netValue?.toDouble() ?: 0.0)
            row.createCell(4).setCellValue(portfolio.stock?.symbol ?: "")
        }

        headers.indices.forEach { sheet.autoSizeColumn(it) }
    }

    // ─── Utilities ───────────────────────────────────────────────────

    private fun createHeaderStyle(workbook: XSSFWorkbook): XSSFCellStyle {
        val style = workbook.createCellStyle()
        val font = workbook.createFont()
        font.bold = true
        font.fontHeightInPoints = 11
        style.setFont(font)
        style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        style.fillPattern = FillPatternType.SOLID_FOREGROUND
        style.borderBottom = BorderStyle.THIN
        style.borderTop = BorderStyle.THIN
        style.borderLeft = BorderStyle.THIN
        style.borderRight = BorderStyle.THIN
        return style
    }

    private fun workbookToBytes(workbook: XSSFWorkbook): ByteArray {
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return outputStream.toByteArray()
    }
}
