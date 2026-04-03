package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PortfolioHistoricalCapitalGainsService(
    private val portfolioRepository: PortfolioRepository
) {
    private val log = LoggerFactory.getLogger(PortfolioHistoricalCapitalGainsService::class.java)

    @Transactional
    fun calculateHistoricalCapitalGains() {
        log.info("Iniciando cálculo de ganancias (plusvalía histórica general) de los portafolios.")

        val portfolios = portfolioRepository.findAll()
        if (portfolios.isEmpty()) return

        for (portfolio in portfolios) {
            val stock = portfolio.stock ?: continue
            val currentPrice = stock.closeDay ?: BigDecimal.ZERO
            
            val avgPrice = portfolio.avgPrice ?: BigDecimal.ZERO
            val quantity = portfolio.totalQuantity ?: BigDecimal.ZERO
            
            val isMxn = stock.currency == "MXN"

            val totalCapitalGains = if (isMxn) {
                // Ganancia calculada directamente para MXN
                if (quantity > BigDecimal.ZERO) {
                    (currentPrice - avgPrice).multiply(quantity)
                } else {
                    BigDecimal.ZERO
                }
            } else {
                // Para otras monedas iteramos las operaciones (regla original)
                val operations = portfolio.operations.sortedBy { it.operationDate }
                var currentQuantityOp = BigDecimal.ZERO
                var totalInvested = BigDecimal.ZERO
                var realizedGains = BigDecimal.ZERO

                for (op in operations) {
                    val qty = op.quantity ?: BigDecimal.ZERO
                    val opPrice = op.price ?: BigDecimal.ZERO

                    if (op.operationType?.name == "buy") {
                        currentQuantityOp += qty
                        totalInvested += op.total ?: BigDecimal.ZERO
                    } else if (op.operationType?.name == "sell") {
                        if (currentQuantityOp > BigDecimal.ZERO) {
                            val avgCost = totalInvested.divide(currentQuantityOp, 6, RoundingMode.HALF_UP)
                            val costOfSold = avgCost.multiply(qty)
                            
                            totalInvested -= costOfSold
                            currentQuantityOp -= qty

                            realizedGains += (opPrice.multiply(qty)) - costOfSold
                        }
                    }
                }

                val unrealizedGains = if (currentQuantityOp > BigDecimal.ZERO) {
                    val marketValue = currentPrice.multiply(currentQuantityOp)
                    marketValue - totalInvested
                } else {
                    BigDecimal.ZERO
                }

                realizedGains + unrealizedGains
            }

            log.info("Portafolio {} ({}): Precio Promedio: {}, Cantidad: {}, Precio Actual: {}, Total Plusvalía Histórica: {}", 
                     portfolio.portfolioId, stock.name, avgPrice.setScale(2, RoundingMode.HALF_UP), quantity, 
                     currentPrice, totalCapitalGains.setScale(2, RoundingMode.HALF_UP))

            portfolio.capitalGains = totalCapitalGains
            portfolioRepository.save(portfolio)
        }
        
        log.info("Finalizó el cálculo de la plusvalía histórica para los portafolios.")
    }
}
