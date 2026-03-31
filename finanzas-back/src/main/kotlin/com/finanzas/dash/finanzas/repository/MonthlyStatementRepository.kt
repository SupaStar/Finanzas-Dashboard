package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.MonthlyStatement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MonthlyStatementRepository : JpaRepository<MonthlyStatement, Long> {
    fun findAllByUser_UserIdOrderByYearDescMonthDesc(userId: Long): List<MonthlyStatement>
    fun findByUser_UserIdAndMonthAndYear(userId: Long, month: Int, year: Int): MonthlyStatement?
}
