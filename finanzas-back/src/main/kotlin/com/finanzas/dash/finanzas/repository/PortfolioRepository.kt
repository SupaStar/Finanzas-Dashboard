package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Portfolio
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.jpa.repository.Modifying

interface PortfolioRepository : JpaRepository<Portfolio, Long> {
    fun findByUserUserId(userId: Long): List<Portfolio>
    fun findByPortfolioId(portfolioId: Long): Portfolio?

//    @EntityGraph(attributePaths = ["dividends"])
//    fun findAllByUserUserId(userId: Long): List<Portfolio>

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.dividends WHERE p.portfolioId = :id")
    fun findByIdWithDividends(id: Long): Portfolio?

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.dividends LEFT JOIN FETCH p.operations")
    fun findAllWithDividendsAndOperations(): List<Portfolio>

    fun findByUserUserIdAndStockSymbol(userId: Long, symbol: String): Portfolio?

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.generalInformation WHERE p.portfolioId = :id")
    fun findByIdWithGeneralInformation(id: Long): Portfolio?

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.generalInformation WHERE p.user.userId = :userId")
    fun findByUserUserIdWithGeneralInformation(userId: Long): List<Portfolio>

    @Modifying
    @Query(
        value = """
            UPDATE portfolio p
            SET n_operations = (
                SELECT CAST(COUNT(operation_id) AS INT) 
                FROM operation o 
                WHERE o.portfolio_id = p.portfolio_id
            )
        """,
        nativeQuery = true
    )
    fun updatePortfolioOperationsCount()
}