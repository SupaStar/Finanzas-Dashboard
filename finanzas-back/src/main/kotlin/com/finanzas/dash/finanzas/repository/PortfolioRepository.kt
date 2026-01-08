package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Portfolio
import org.springframework.data.jpa.repository.JpaRepository

interface PortfolioRepository : JpaRepository<Portfolio, Int> {
}