package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.fixed_portfolio.CreateFixedPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.request.fixed_portfolio.UpdateFixedPortfolioAmountDto
import com.finanzas.dash.finanzas.dto.response.fixed_instrument.FixedInstrumentResponseDto
import com.finanzas.dash.finanzas.dto.response.fixed_portfolio.FixedPortfolioResponseDto
import com.finanzas.dash.finanzas.entity.FixedPortfolio
import com.finanzas.dash.finanzas.repository.FixedInstrumentRepository
import com.finanzas.dash.finanzas.repository.FixedPortfolioRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.finanzas.dash.finanzas.entity.DailyPay
import com.finanzas.dash.finanzas.repository.DailyPayRepository
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class FixedPortfolioService(
    private val securityService: SecurityService,
    private val fixedPortfolioRepository: FixedPortfolioRepository,
    private val fixedInstrumentRepository: FixedInstrumentRepository,
    private val dailyPayRepository: DailyPayRepository
) {

    fun getAllFixedPortfoliosByUser(): List<FixedPortfolioResponseDto> {
        val user = securityService.currentUser()
        return fixedPortfolioRepository.findAllByUserAndDeletedFalse(user).map {
            FixedPortfolioResponseDto(
                id = it.id!!,
                fixedInstrument = FixedInstrumentResponseDto(
                    id = it.fixedInstrument!!.id!!,
                    name = it.fixedInstrument!!.name!!,
                    anualRate = it.fixedInstrument!!.anualRate!!
                ),
                amount = it.amount!!
            )
        }
    }

    @Transactional
    fun createFixedPortfolio(dto: CreateFixedPortfolioRequestDto): FixedPortfolioResponseDto {
        val user = securityService.currentUser()
        val instrument = fixedInstrumentRepository.findById(dto.fixedInstrumentId!!)
            .orElseThrow { GeneralRequestException(listOf("FixedInstrument not found"), HttpStatus.NOT_FOUND) }

        val portfolio = FixedPortfolio().apply {
            this.fixedInstrument = instrument
            this.user = user
            this.amount = dto.amount
            this.deleted = false
        }
        val saved = fixedPortfolioRepository.save(portfolio)
        
        return FixedPortfolioResponseDto(
            id = saved.id!!,
            fixedInstrument = FixedInstrumentResponseDto(
                id = instrument.id!!,
                name = instrument.name!!,
                anualRate = instrument.anualRate!!
            ),
            amount = saved.amount!!
        )
    }

    @Transactional
    fun updateFixedPortfolioAmount(id: Long, dto: UpdateFixedPortfolioAmountDto): FixedPortfolioResponseDto {
        val user = securityService.currentUser()
        val portfolio = fixedPortfolioRepository.findById(id)
            .orElseThrow { GeneralRequestException(listOf("FixedPortfolio not found"), HttpStatus.NOT_FOUND) }
            
        if (portfolio.user?.userId != user.userId) {
            throw GeneralRequestException(listOf("No tienes permisos para editar este portafolio"), HttpStatus.FORBIDDEN)
        }

        portfolio.amount = dto.amount ?: portfolio.amount
        val saved = fixedPortfolioRepository.save(portfolio)
        
        return FixedPortfolioResponseDto(
            id = saved.id!!,
            fixedInstrument = FixedInstrumentResponseDto(
                id = saved.fixedInstrument!!.id!!,
                name = saved.fixedInstrument!!.name!!,
                anualRate = saved.fixedInstrument!!.anualRate!!
            ),
            amount = saved.amount!!
        )
    }

    @Transactional
    fun deleteFixedPortfolioLogically(id: Long) {
        val user = securityService.currentUser()
        val portfolio = fixedPortfolioRepository.findById(id)
            .orElseThrow { GeneralRequestException(listOf("FixedPortfolio not found"), HttpStatus.NOT_FOUND) }
            
        if (portfolio.user?.userId != user.userId) {
            throw GeneralRequestException(listOf("No tienes permisos para eliminar este portafolio"), HttpStatus.FORBIDDEN)
        }

        portfolio.deleted = true
        fixedPortfolioRepository.save(portfolio)
    }

    @Transactional
    fun calculateDailyInterest() {
        // Fetch all active fixed portfolios
        val portfolios = fixedPortfolioRepository.findAllByDeletedFalse()
        
        for (portfolio in portfolios) {
            val amount = portfolio.amount ?: BigDecimal.ZERO
            val anualRate = portfolio.fixedInstrument?.anualRate ?: BigDecimal.ZERO
            
            // Formula specified by user: amount * (anual_rate / 100) / 365
            if (amount > BigDecimal.ZERO && anualRate > BigDecimal.ZERO) {
                // Determine interest
                val rateDecimal = anualRate.divide(BigDecimal("100"), 6, RoundingMode.HALF_UP)
                val annualInterest = amount.multiply(rateDecimal)
                val dailyInterest = annualInterest.divide(BigDecimal("365"), 6, RoundingMode.HALF_UP)
                
                if (dailyInterest > BigDecimal.ZERO) {
                    // Record daily pay
                    val dailyPay = DailyPay().apply {
                        this.fixedPortfolio = portfolio
                        this.amount = dailyInterest
                        this.anualRateCalculated = anualRate
                    }
                    dailyPayRepository.save(dailyPay)
                    
                    // Compound interest (add to current amount)
                    portfolio.amount = amount.add(dailyInterest)
                    fixedPortfolioRepository.save(portfolio)
                }
            }
        }
    }
}
