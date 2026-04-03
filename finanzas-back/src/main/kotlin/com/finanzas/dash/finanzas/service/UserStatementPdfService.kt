package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.User
import com.lowagie.text.Document
import com.lowagie.text.FontFactory
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.Element

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
class UserStatementPdfService {

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
        val document = Document()
        
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            
            // Título
            val titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
            val title = Paragraph("Estado de Cuenta Mensual", titleFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)
            
            document.add(Paragraph("\n"))
            
            // Info Header
            val headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12f)
            document.add(Paragraph("Usuario: ${user.username}", headerFont))
            document.add(Paragraph("Periodo: ${YearMonth.of(year, month)}", headerFont))
            document.add(Paragraph("\n"))
            
            var totalCapitalGains = BigDecimal.ZERO
            var totalFees = BigDecimal.ZERO
            var totalTaxes = BigDecimal.ZERO
            var totalDividends = BigDecimal.ZERO

            // Stocks Section
            if (stockRows.isNotEmpty()) {
                val subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f)
                document.add(Paragraph("Portafolio de Acciones", subTitleFont))
                document.add(Paragraph("\n"))
                
                val table = PdfPTable(4)
                table.widthPercentage = 100f
                
                addTableHeader(table, listOf("Acción", "Invested", "Market Value", "Plusvalía del Mes"))
                
                for (row in stockRows) {
                    table.addCell(row.stockName)
                    table.addCell("$${row.totalInvested.setScale(2, RoundingMode.HALF_UP)}")
                    table.addCell("$${row.marketValue.setScale(2, RoundingMode.HALF_UP)}")
                    table.addCell("$${row.capitalGains.setScale(2, RoundingMode.HALF_UP)}")
                    
                    totalCapitalGains += row.capitalGains
                    totalFees += row.fees
                    totalTaxes += row.taxes
                    totalDividends += row.dividends
                }
                
                document.add(table)
                document.add(Paragraph("\n"))
            }

            var totalFixedIntercept = BigDecimal.ZERO

            // Fixed Portfolio Section
            if (fixedRows.isNotEmpty()) {
                val subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f)
                document.add(Paragraph("Portafolio de Renta Fija", subTitleFont))
                document.add(Paragraph("\n"))
                
                val table = PdfPTable(3)
                table.widthPercentage = 100f
                
                addTableHeader(table, listOf("Instrumento", "Invertido", "Interés Generado"))
                
                for (row in fixedRows) {
                    table.addCell(row.instrumentName)
                    table.addCell("$${row.investedAmount.setScale(2, RoundingMode.HALF_UP)}")
                    table.addCell("$${row.monthlyInterestGenerated.setScale(2, RoundingMode.HALF_UP)}")
                    
                    totalFixedIntercept += row.monthlyInterestGenerated
                }
                
                document.add(table)
                document.add(Paragraph("\n"))
            }
            
            // Condicional de Intereses, Comisiones e Impuestos
            if (totalFees > BigDecimal.ZERO || totalTaxes > BigDecimal.ZERO || totalDividends > BigDecimal.ZERO) {
                val conditionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)
                document.add(Paragraph("Resumen de Comisiones, Impuestos y Dividendos", conditionFont))
                document.add(Paragraph("\n"))
                
                val feeTable = PdfPTable(3)
                feeTable.widthPercentage = 100f
                addTableHeader(feeTable, listOf("Monto Comisiones", "Monto Impuestos", "Monto Dividendos Pagados"))
                
                feeTable.addCell("$${totalFees.setScale(2, RoundingMode.HALF_UP)}")
                feeTable.addCell("$${totalTaxes.setScale(2, RoundingMode.HALF_UP)}")
                feeTable.addCell("$${totalDividends.setScale(2, RoundingMode.HALF_UP)}")
                
                document.add(feeTable)
                document.add(Paragraph("\n"))
            }
            
            // Total Mensual Resumen
            val summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f)
            val grandTotalItem = totalCapitalGains + totalFixedIntercept + totalDividends - totalFees - totalTaxes
            
            document.add(Paragraph("RESUMEN GENERAL", summaryFont))
            document.add(Paragraph("Plusvalía Acciones: $${totalCapitalGains.setScale(2, RoundingMode.HALF_UP)}"))
            document.add(Paragraph("Intereses Fijos Generados: $${totalFixedIntercept.setScale(2, RoundingMode.HALF_UP)}"))
            document.add(Paragraph("Grand Total Balance del Mes (Plusvalia + Fixed + Divs - Gastos): $${grandTotalItem.setScale(2, RoundingMode.HALF_UP)}"))
            
            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error creando el estado de cuenta PDF")
        }
        
        return fileName
    }
    
    private fun addTableHeader(table: PdfPTable, headers: List<String>) {
        val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        headers.forEach { headerTitle ->
            val cell = PdfPCell()
            cell.phrase = Paragraph(headerTitle, font)
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }
    }
}
