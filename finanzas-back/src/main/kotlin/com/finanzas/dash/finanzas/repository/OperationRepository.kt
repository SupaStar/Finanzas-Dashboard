package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Operation
import org.springframework.data.jpa.repository.JpaRepository

interface OperationRepository : JpaRepository<Operation, Long> {
    fun findByPortfolioPortfolioId(portfolioId: Long): List<Operation>
}