package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.dividend.AddDividendPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.dividend.AddDividendResponseDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendsPortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Dividend
import com.finanzas.dash.finanzas.repository.DividendRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class DividendService(
    private val dividendRepository: DividendRepository,
    private val portfolioRepository: PortfolioRepository,
    private val portfolioService: PortfolioService
) {
    fun getAllDividendsPorfolio(portfolioId: Long): DividendsPortfolioResponseDto {
        val dividends = dividendRepository.findByPortfolioPortfolioId(portfolioId)
        return DividendsPortfolioResponseDto(message = dividends.map { it.toDto() })
    }

    fun addDividend(portfolioId: Long, request: AddDividendPortfolioRequestDto): AddDividendResponseDto {
        val portfolio = portfolioRepository.findByPortfolioId(portfolioId)
        try {
            val dividend = dividendRepository.save(Dividend().apply {
                this.dividendType = request.dividendType
                this.value = request.value
                this.portfolio = portfolio
                this.modifiedAt = OffsetDateTime.now()
                this.paidDate = LocalDate.parse(request.paidDate!!).atStartOfDay().atOffset(ZoneOffset.UTC)
                this.currencyCode = request.currencyCode
                this.tax = request.tax!!
                this.netValue =  request.value!! * request.exchangeRate!!
                this.exchangeRate = request.exchangeRate!!
            })
            portfolioService.updatePortfolioData(portfolioId)
            return AddDividendResponseDto(message = dividend.toDto())
        } catch (ex: Exception) {
            System.err.println("Error al guardar operation " + ex.message)
            throw GeneralRequestException(
                errors = listOf("Ocurrio un error al intentar guardar la operacion."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}