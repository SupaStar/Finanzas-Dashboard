package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.response.daily_pay.DailyPayResponseDto
import com.finanzas.dash.finanzas.repository.DailyPayRepository
import com.finanzas.dash.finanzas.repository.FixedPortfolioRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class DailyPayService(
    private val securityService: SecurityService,
    private val fixedPortfolioRepository: FixedPortfolioRepository,
    private val dailyPayRepository: DailyPayRepository
) {

    fun getDailyPaysByPortfolio(portfolioId: Long): List<DailyPayResponseDto> {
        val user = securityService.currentUser()
        val portfolio = fixedPortfolioRepository.findById(portfolioId)
            .orElseThrow { GeneralRequestException(listOf("FixedPortfolio not found"), HttpStatus.NOT_FOUND) }
            
        if (portfolio.user?.userId != user.userId) {
            throw GeneralRequestException(listOf("No tienes permisos para ver este portafolio"), HttpStatus.FORBIDDEN)
        }

        return dailyPayRepository.findAllByFixedPortfolio(portfolio).map {
            DailyPayResponseDto(
                id = it.id!!,
                fixedPortfolioId = portfolio.id!!,
                amount = it.amount!!,
                anualRateCalculated = it.anualRateCalculated!!
            )
        }
    }
}
