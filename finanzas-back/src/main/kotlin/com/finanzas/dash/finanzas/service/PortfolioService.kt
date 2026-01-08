package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.portfolio.AddStockPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioGetAllResponseDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.repository.StockRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PortfolioService(
    private val securityService: SecurityService,
    private val portfolioRepository: PortfolioRepository,
    private val stockRepository: StockRepository
) {
    fun test() {
        val user = securityService.currentUser()
        val owo = ""
    }

    fun addStockUserPortfolio(requestDto: AddStockPortfolioRequestDto): PortfolioResponseDto {
        val user = securityService.currentUser()
        val stock = stockRepository.findByStockId(requestDto.stockID!!) ?: throw GeneralRequestException(
            listOf("Error al encontrar la accion"),
            HttpStatus.NOT_FOUND
        )
        try {
            val portfolioItem =portfolioRepository.save(Portfolio().apply {
                this.user = user
                this.stock = stock
                this.avgPrice = BigDecimal.ZERO
                this.totalQuantity = BigDecimal.ZERO
            })
            return PortfolioResponseDto(estado = true, messafe = portfolioItem.toDto())
        } catch (ex: DataIntegrityViolationException) {
            throw GeneralRequestException(listOf("Accion ya registrada"), HttpStatus.CONFLICT)
//            throw GeneralRequestException(listOf(ex.message!!), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun getUserPortfolio(): PortfolioGetAllResponseDto {
        val user = securityService.currentUser()
        val portfolio = portfolioRepository.findByUserUserId(user.userId!!)
        return PortfolioGetAllResponseDto(messafe = portfolio.map { it.toDto() })
    }
}