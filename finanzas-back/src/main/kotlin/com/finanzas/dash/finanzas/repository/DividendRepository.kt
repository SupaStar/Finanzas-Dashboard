package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Dividend
import org.springframework.data.jpa.repository.JpaRepository

import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface DividendRepository : JpaRepository<Dividend, Long> {
    fun findByPortfolioPortfolioId(portfolioId: Long): List<Dividend>

    @Query("SELECT d FROM Dividend d WHERE d.portfolio.user.userId = :userId AND d.paidDate >= :startDate AND d.paidDate <= :endDate")
    fun findByUserAndDateRange(userId: Long, startDate: OffsetDateTime, endDate: OffsetDateTime): List<Dividend>
}