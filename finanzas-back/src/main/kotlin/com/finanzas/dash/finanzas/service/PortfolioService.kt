package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.portfolio.AddStockPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioGetAllResponseDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import com.finanzas.dash.finanzas.repository.DividendRepository
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
    private val stockRepository: StockRepository,
    private val dividendRepository: DividendRepository,
    private val utilService: UtilService,
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
            return PortfolioResponseDto(estado = true, message = portfolioItem.toDto())
        } catch (ex: DataIntegrityViolationException) {
            throw GeneralRequestException(listOf("Accion ya registrada"), HttpStatus.CONFLICT)
//            throw GeneralRequestException(listOf(ex.message!!), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun getUserPortfolio(): PortfolioGetAllResponseDto {
        val usdPrice = utilService.getUsdValue()
        val user = securityService.currentUser()
        val portfolio = portfolioRepository.findByUserUserIdWithGeneralInformation(user.userId!!)
        return PortfolioGetAllResponseDto(message = portfolio.map { it.toDto() }, usdPrice = usdPrice.USD_MXN)
    }

    fun getSingleUserPortfolio(portfolioId: Long): PortfolioGetAllResponseDto {
        val usdPrice = utilService.getUsdValue()
        val user = securityService.currentUser()
        val portfolioInfo = portfolioRepository.findByIdWithGeneralInformation(portfolioId)
            ?: throw GeneralRequestException(
                listOf("Portafolio no encontrado"),
                HttpStatus.NOT_FOUND
            )

        if (portfolioInfo.user?.userId != user.userId) {
            throw GeneralRequestException(
                listOf("No tienes permisos para ver este portafolio"),
                HttpStatus.FORBIDDEN
            )
        }

        return PortfolioGetAllResponseDto(
            message = listOf(portfolioInfo.toDto()),
            usdPrice = usdPrice.USD_MXN
        )
    }

    @Transactional
    fun updatePortfolioData(portfolioId: Long) {
        val portfolio =
            portfolioRepository.findByIdWithDividends(portfolioId) ?: throw GeneralRequestException(
                listOf("Error al encontrar el portafolio"),
                HttpStatus.BAD_REQUEST
            )
        val (buyOps, sellOps) = portfolio.operations.partition {
            it.operationType == OperationTypeEnum.buy
        }

        val operationsTotalBuy = buyOps.sumOf {
            (it.quantity ?: BigDecimal.ZERO)
        }

        val operationsTotalSell = sellOps.sumOf {
            (it.quantity ?: BigDecimal.ZERO)
        }

        val operationsAmountBuy = buyOps.sumOf {
            (it.quantity ?: BigDecimal.ZERO) * (it.price ?: BigDecimal.ZERO)
        }

        val operationsAmountSell = sellOps.sumOf {
            (it.quantity ?: BigDecimal.ZERO) * (it.price ?: BigDecimal.ZERO)
        }

        val operationsTotal = operationsTotalBuy.subtract(operationsTotalSell)
        var operationsAmount = operationsAmountBuy.subtract(operationsAmountSell)

        val (dividendsReinvested, dividendsCash) = portfolio.dividends.partition {
            it.dividendType == DividendTypeEnum.reinvested
        }

        val totalDividendsRein = dividendsReinvested.sumOf {
            (it.netValue ?: BigDecimal.ZERO)
        }

        operationsAmount = operationsAmount.subtract(totalDividendsRein)
        val avgPrice = if (operationsTotal > BigDecimal.ZERO) {
            operationsAmount.divide(operationsTotal, 4, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }

        portfolio.totalQuantity = operationsTotal
        portfolio.avgPrice = avgPrice

        portfolioRepository.save(portfolio)
    }

    fun updateAllDividends(portfolioId: Long){
        val portfolio =
            portfolioRepository.findByIdWithDividends(portfolioId) ?: throw GeneralRequestException(
                listOf("Error al encontrar el portafolio"),
                HttpStatus.BAD_REQUEST
            )

        portfolio.dividends.forEach { dividend ->
            dividend.netValue = dividend.value!! * dividend.exchangeRate!! - dividend.tax
            dividendRepository.save(dividend)
        }
    }

    fun updateAllPortfolios(userId: Long) {
        val portfolios = portfolioRepository.findByUserUserId(userId)
        portfolios.forEach { portfolio ->
            updatePortfolioData(portfolio.portfolioId!!)
        }
    }
}