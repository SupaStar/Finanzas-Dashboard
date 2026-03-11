package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.DailyPay
import com.finanzas.dash.finanzas.entity.FixedPortfolio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DailyPayRepository : JpaRepository<DailyPay, Long> {
    fun findAllByFixedPortfolio(fixedPortfolio: FixedPortfolio): List<DailyPay>
}
