package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.dto.request.stocks.StockHistoricalRequestDto
import com.finanzas.dash.finanzas.entity.PortfolioGeneralInformation
import com.finanzas.dash.finanzas.repository.PortfolioGeneralInformationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.YearMonth

@Service
class PortfolioMonthlyCapitalGainsService(
    private val portfolioRepository: PortfolioRepository,
    private val portfolioGeneralInformationRepository: PortfolioGeneralInformationRepository,
    private val stockApiService: StockApiService,
    private val monthlyStatementFacadeService: MonthlyStatementFacadeService
) {
    private val log = LoggerFactory.getLogger(PortfolioMonthlyCapitalGainsService::class.java)

    @Transactional
    fun calculateMonthlyCapitalGains() {
        // Obtenemos el mes anterior al mes actual en curso
        val today = LocalDate.now()
        val previousMonth = YearMonth.of(today.year, today.monthValue).minusMonths(1)
        val startDate = previousMonth.atDay(1)
        val endDate = previousMonth.atEndOfMonth()
        val endDateStr = endDate.toString()
        val startDateStr = startDate.toString()
        
        log.info("Calculando plusvalía general para el mes de {}-{}", previousMonth.year, previousMonth.monthValue)

        // Todos los portafolios (una iteracion simple, idealmente deberia ser por lotes para bases grandes)
        val portfolios = portfolioRepository.findAll()
        
        // Obtener todos los símbolos unicos
        val activeStocks = portfolios.mapNotNull { it.stock?.name }.distinct()
        if (activeStocks.isEmpty()) return

        // Extraer historico del mes de py-stock
        val historicalRequest = StockHistoricalRequestDto(
            tickers = activeStocks,
            start = startDateStr,
            end = endDateStr
        )
        val historicalData = try {
            stockApiService.getHistoricalPrices(historicalRequest)
        } catch (e: Exception) {
            log.error("Error al obtener historico de pystock", e)
            return
        }

        // Procesar por portfolio
        for (portfolio in portfolios) {
            val stockName = portfolio.stock?.name ?: continue
            val pricesForStock = historicalData[stockName] ?: continue

            // Obtener todas las operaciones hasta el final del mes calculo
            val allOperationsBeforeEndOfMonth = portfolio.operations
                .filter { it.operationDate != null && !it.operationDate!!.toLocalDate().isAfter(endDate) }
                .sortedBy { it.operationDate }

            if (allOperationsBeforeEndOfMonth.isEmpty()) continue

            // Calcular el general "capitalGains" a final de ese mes.
            var currentQuantity = BigDecimal.ZERO
            var totalInvested = BigDecimal.ZERO
            var realizedGains = BigDecimal.ZERO

            // Para recorrer e imprimir las plusvalias individuales en logs segun indicacion
            val dailyGainsLog = StringBuilder()

            for (op in allOperationsBeforeEndOfMonth) {
                if (op.operationType?.name == "buy") {
                    currentQuantity += op.quantity ?: BigDecimal.ZERO
                    totalInvested += op.total ?: BigDecimal.ZERO
                    val opDate = op.operationDate!!.toLocalDate()

                    // Solo para logging
                    if (!opDate.isBefore(startDate)) {
                        val closePriceAtEndStr = pricesForStock[endDateStr]?.toString() ?: pricesForStock.values.lastOrNull()?.toString()
                        if (closePriceAtEndStr != null) {
                            val closePriceAtEnd = BigDecimal(closePriceAtEndStr)
                            val opPrice = op.price ?: BigDecimal.ZERO
                            val gain = (closePriceAtEnd - opPrice).multiply(op.quantity ?: BigDecimal.ZERO)
                            dailyGainsLog.append(" [Compra ${opDate} @ \$${opPrice}: Plusvalía en mes = \$${gain.setScale(2, RoundingMode.HALF_UP)}] ")
                        }
                    }
                } else if (op.operationType?.name == "sell") {
                    val qtyToSell = op.quantity ?: BigDecimal.ZERO
                    if (currentQuantity > BigDecimal.ZERO) {
                        val avgCost = totalInvested.divide(currentQuantity, 6, RoundingMode.HALF_UP)
                        val costOfSold = avgCost.multiply(qtyToSell)
                        totalInvested -= costOfSold
                        currentQuantity -= qtyToSell
                        
                        val opPrice = op.price ?: BigDecimal.ZERO
                        realizedGains += (opPrice.multiply(qtyToSell)) - costOfSold
                    }
                }
            }
            if (dailyGainsLog.isNotEmpty()) {
                log.info("Portfolio {} ({}):{}", portfolio.portfolioId, stockName, dailyGainsLog.toString())
            }

            // Calculo de la plusvalia/minusvalia general a fin de mes
            val finalMonthPriceStr = pricesForStock[endDateStr]?.toString() ?: pricesForStock.values.lastOrNull()?.toString()
            if (finalMonthPriceStr != null && currentQuantity >= BigDecimal.ZERO) {
                val finalMonthPrice = BigDecimal(finalMonthPriceStr)
                val isMxn = portfolio.stock?.currency == "MXN"
                val avgPriceForMonthEnd = if (currentQuantity > BigDecimal.ZERO) totalInvested.divide(currentQuantity, 6, RoundingMode.HALF_UP) else BigDecimal.ZERO

                val totalPlusvalia = if (isMxn && currentQuantity > BigDecimal.ZERO) {
                    (finalMonthPrice - avgPriceForMonthEnd).multiply(currentQuantity)
                } else if (isMxn) {
                    BigDecimal.ZERO
                } else {
                    val marketValue = finalMonthPrice.multiply(currentQuantity)
                    val unrealizedGains = marketValue - totalInvested
                    realizedGains + unrealizedGains
                }

                // Upsert to PortfolioGeneralInformation
                val existingInfo = portfolioGeneralInformationRepository
                    .findAll()
                    .firstOrNull { it.portfolio?.portfolioId == portfolio.portfolioId && it.year == previousMonth.year && it.month == previousMonth.monthValue }
                
                if (existingInfo != null) {
                    existingInfo.capitalGains = totalPlusvalia
                    portfolioGeneralInformationRepository.save(existingInfo)
                } else {
                    val newInfo = PortfolioGeneralInformation().apply {
                        this.portfolio = portfolio
                        this.year = previousMonth.year
                        this.month = previousMonth.monthValue
                        this.capitalGains = totalPlusvalia
                        this.dividendsTotal = BigDecimal.ZERO
                    }
                    portfolioGeneralInformationRepository.save(newInfo)
                }
            }
        }
        
        log.info("Finalizó calculo de plusvalía general mensual")

        // Disparar generación del Estado de Cuenta PDF
        try {
            log.info("Iniciando generación de estados de cuenta PDF para usuarios...")
            monthlyStatementFacadeService.generateStatementsForPreviousMonth()
            log.info("Finalizó generación de estados de cuenta PDF")
        } catch(e: Exception) {
            log.error("Error global disparando la generación de PDFs: \${e.message}", e)
        }
    }
}
