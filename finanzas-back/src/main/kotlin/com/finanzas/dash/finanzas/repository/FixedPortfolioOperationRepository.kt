package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.FixedPortfolio
import com.finanzas.dash.finanzas.entity.FixedPortfolioOperation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixedPortfolioOperationRepository : JpaRepository<FixedPortfolioOperation, Long> {
    fun findAllByFixedPortfolioOrderByOperationDateDesc(fixedPortfolio: FixedPortfolio): List<FixedPortfolioOperation>
}
