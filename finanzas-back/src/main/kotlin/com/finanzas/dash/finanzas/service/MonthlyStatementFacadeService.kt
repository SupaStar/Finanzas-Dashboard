package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.repository.UserRepository
import com.finanzas.dash.finanzas.repository.PortfolioGeneralInformationRepository
import com.finanzas.dash.finanzas.repository.FixedPortfolioRepository
import com.finanzas.dash.finanzas.repository.DailyPayRepository
import com.finanzas.dash.finanzas.repository.MonthlyStatementRepository
import com.finanzas.dash.finanzas.entity.MonthlyStatement
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.YearMonth

@Service
class MonthlyStatementFacadeService(
    private val userRepository: UserRepository,
    private val portfolioGeneralInformationRepository: PortfolioGeneralInformationRepository,
    private val fixedPortfolioRepository: FixedPortfolioRepository,
    private val dailyPayRepository: DailyPayRepository,
    private val statementPdfService: UserStatementPdfService,
    private val monthlyStatementRepository: MonthlyStatementRepository
) {
    private val log = LoggerFactory.getLogger(MonthlyStatementFacadeService::class.java)

    @Transactional(readOnly = true)
    fun generateStatementsForPreviousMonth() {
        val previousMonthDate = LocalDate.now().minusMonths(1)
        val year = previousMonthDate.year
        val month = previousMonthDate.monthValue

        val users = userRepository.findAll()
        val generatedPath = "statements" // relative path

        for (user in users) {
            val stockRows = mutableListOf<StockStatementRow>()
            val fixedRows = mutableListOf<FixedStatementRow>()

            var hasTransactionsOrInfo = false

            // 1. Recopilar Información de Acciones (del Job Mensual y Operaciones del mes)
            val infoList = portfolioGeneralInformationRepository.findAll()
                .filter { it.portfolio?.user?.userId == user.userId && it.year == year && it.month == month }

            for (info in infoList) {
                val pt = info.portfolio ?: continue
                val stockName = pt.stock?.name ?: "Unknown"

                val capitalGains = info.capitalGains ?: BigDecimal.ZERO
                val dividends = info.dividendsTotal ?: BigDecimal.ZERO

                // Buscar operaciones históricas HASTA el fin del mes analizado para obtener el balance exácto invertido a esa fecha.
                var currentQuantityOp = BigDecimal.ZERO
                var totalInvested = BigDecimal.ZERO

                var monthlyFees = BigDecimal.ZERO
                var monthlyTaxes = BigDecimal.ZERO
                
                val endOfMonthDate = YearMonth.of(year, month).atEndOfMonth()

                pt.operations
                    .filter { it.operationDate != null && !it.operationDate!!.toLocalDate().isAfter(endOfMonthDate) }
                    .sortedBy { it.operationDate }
                    .forEach { op ->
                        val opDate = op.operationDate!!.toLocalDate()
                        if (opDate.year == year && opDate.monthValue == month) {
                            monthlyFees += op.fee
                            monthlyTaxes += op.tax
                        }
                        
                        val qty = op.quantity ?: BigDecimal.ZERO
                        if (op.operationType?.name == "buy") {
                            currentQuantityOp += qty
                            totalInvested += op.total ?: BigDecimal.ZERO
                        } else if (op.operationType?.name == "sell") {
                            if (currentQuantityOp > BigDecimal.ZERO) {
                                val avgCost = totalInvested.divide(currentQuantityOp, 6, RoundingMode.HALF_UP)
                                val costOfSold = avgCost.multiply(qty)
                                totalInvested -= costOfSold
                                currentQuantityOp -= qty
                            }
                        }
                    }

                // Si la cantidad calculada en base a las operaciones del mes es cero pero hay transacciones, 
                // MarketValue será 0 y totalInvested será 0 (asumiendo que vació su portafolio)
                val isMxn = pt.stock?.currency == "MXN"
                val pricesOfMonthEnd = pt.stock?.closeDay ?: BigDecimal.ZERO // TODO: Ojo, idealmente usaríamos el histórico pero pt.stock.closeDay nos ayuda por ahora (o el py-stock snapshot). 
                
                // Realmente obtenemos el MarketValue con la cantidad "currentQuantityOp" a final del mes
                val endMarketValue = currentQuantityOp.multiply(pricesOfMonthEnd)
                
                // Como indicaste sobre no usar precios anteriores en lo posible y aplicar reglas por moneda.
                // Usaremos lo que está en PortfolioGeneralInformation como plusvalía oficial `capitalGains`.

                if (currentQuantityOp > BigDecimal.ZERO || monthlyFees > BigDecimal.ZERO || dividends > BigDecimal.ZERO || capitalGains != BigDecimal.ZERO) {
                    stockRows.add(
                        StockStatementRow(
                            stockName = stockName,
                            totalInvested = totalInvested, // El net invertido al final del mes
                            marketValue = endMarketValue,  // El market value estimado al final del mes
                            capitalGains = capitalGains,   // Extraído directo de la Base de Datos (calculado por tu Job)

                            fees = monthlyFees,
                            taxes = monthlyTaxes,
                            dividends = dividends
                        )
                    )
                    hasTransactionsOrInfo = true
                }
            }

            // 2. Recopilar Información Fija (Rendimientos generados este mes)
            val fixedPortfolios = fixedPortfolioRepository.findAll().filter { it.user?.userId == user.userId && !it.deleted }
            for (fp in fixedPortfolios) {
                val paysInMonth = dailyPayRepository.findAllByFixedPortfolio(fp).filter {
                    it.payDate.year == year && it.payDate.monthValue == month
                }
                
                var monthYield = BigDecimal.ZERO
                paysInMonth.forEach { monthYield += (it.amount ?: BigDecimal.ZERO) }

                val fpAmountSafe = fp.amount ?: BigDecimal.ZERO
                if (fpAmountSafe > BigDecimal.ZERO || monthYield > BigDecimal.ZERO) {
                    fixedRows.add(
                        FixedStatementRow(
                            instrumentName = fp.fixedInstrument?.name ?: "Fixed",
                            investedAmount = fp.amount ?: BigDecimal.ZERO,
                            monthlyInterestGenerated = monthYield
                        )
                    )
                    hasTransactionsOrInfo = true
                }
            }

            // Solo generamos estado de cuenta si tienen algo asociado
            if (hasTransactionsOrInfo) {
                try {
                    val fileName = statementPdfService.generateMonthlyStatement(user, month, year, stockRows, fixedRows, generatedPath)
                    
                    val existingStatement = monthlyStatementRepository.findByUser_UserIdAndMonthAndYear(user.userId!!, month, year)
                    if (existingStatement == null) {
                        monthlyStatementRepository.save(MonthlyStatement().apply {
                            this.user = user
                            this.month = month
                            this.year = year
                            this.filePath = "$generatedPath/$fileName"
                        })
                    } else {
                        existingStatement.filePath = "$generatedPath/$fileName"
                        monthlyStatementRepository.save(existingStatement)
                    }

                    log.info("Estado de cuenta generado para usuario ID: ${user.userId}")
                } catch (e: Exception) {
                    log.error("Error al generar PDF para usuario ${user.userId}: ${e.message}")
                }
            }
        }
    }
}
