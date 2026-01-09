package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.portfolio.AddStockPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioGetAllResponseDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.repository.StockRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

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
            val portfolioItem = portfolioRepository.save(Portfolio().apply {
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

    @Transactional
    fun updateValueOperation(operation: Operation) {
        val portfolio =
            portfolioRepository.findByPortfolioId(operation.portfolio?.portfolioId!!) ?: throw GeneralRequestException(
                listOf("Error al encontrar el portafolio"),
                HttpStatus.BAD_REQUEST
            )
        val currentQuantity = portfolio.totalQuantity ?: BigDecimal.ZERO
        val operationQuantity = operation.quantity ?: BigDecimal.ZERO
        if (operation.operationType == OperationTypeEnum.buy) {
            currentQuantity.add(operationQuantity)
        } else {
            currentQuantity.subtract(operationQuantity)
        }
        val totalReinversionQty = portfolio.dividends
            .filter { it.dividendType == DividendTypeEnum.reinvested }
            .map { it.value ?: BigDecimal.ZERO }
            .reduce { acc, value -> acc.add(value) } ?: BigDecimal.ZERO
        val newQuantity = currentQuantity.add(operation.quantity ?: BigDecimal.ZERO).subtract(totalReinversionQty)
        if (newQuantity > BigDecimal.ZERO) {
            val totalCost = (currentQuantity.multiply(portfolio.avgPrice ?: BigDecimal.ZERO))
                .add((operation.quantity ?: BigDecimal.ZERO).multiply(operation.price ?: BigDecimal.ZERO))

            portfolio.avgPrice = totalCost.divide(newQuantity, 6, RoundingMode.HALF_UP)
        }

        portfolio.totalQuantity = newQuantity
    }
}