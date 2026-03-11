package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.FixedPortfolio
import com.finanzas.dash.finanzas.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixedPortfolioRepository : JpaRepository<FixedPortfolio, Long> {
    fun findAllByUserAndDeletedFalse(user: User): List<FixedPortfolio>
    fun findAllByDeletedFalse(): List<FixedPortfolio>
}
