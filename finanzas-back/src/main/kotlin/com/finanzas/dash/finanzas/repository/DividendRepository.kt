package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Dividend
import org.springframework.data.jpa.repository.JpaRepository

interface DividendRepository : JpaRepository<Dividend, Long> {
    fun findByPortfolioPortfolioId(portfolioId: Long): List<Dividend>
}