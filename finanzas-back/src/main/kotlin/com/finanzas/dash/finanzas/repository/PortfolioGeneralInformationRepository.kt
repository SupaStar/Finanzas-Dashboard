package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.PortfolioGeneralInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PortfolioGeneralInformationRepository : JpaRepository<PortfolioGeneralInformation, Long> {
    fun findByPortfolioPortfolioIdAndYearAndMonth(portfolioId: Long, year: Int, month: Int): PortfolioGeneralInformation?

    // Update dividends total with native SQL
    @Modifying
    @Query(
        value = """
            INSERT INTO portfolio_general_information (portfolio_id, year, month, dividends_total)
            SELECT
                portfolio_id,
                EXTRACT(YEAR FROM COALESCE(paid_date, created_at)) as year,
                EXTRACT(MONTH FROM COALESCE(paid_date, created_at)) as month,
                SUM(net_value) as dividends_total
            FROM dividend
            GROUP BY portfolio_id, year, month
            ON CONFLICT (portfolio_id, year, month)
            DO UPDATE SET dividends_total = EXCLUDED.dividends_total
        """,
        nativeQuery = true
    )
    fun upsertDividendsTotal()
}
