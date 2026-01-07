package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigInteger

interface StockRepository : JpaRepository<Stock, BigInteger> {
    fun findByName(name: String): Stock?
}