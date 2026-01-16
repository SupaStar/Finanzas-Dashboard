package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Portfolio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PortfolioRepository : JpaRepository<Portfolio, Long> {
    fun findByUserUserId(userId: Long): List<Portfolio>
    fun findByPortfolioId(portfolioId: Long): Portfolio?

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.dividends WHERE p.portfolioId = :id")
    fun findByIdWithDividends(id: Long): Portfolio?

    fun findByUserUserIdAndStockSymbol(userId: Long, symbol: String): Portfolio?
}