package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.dividend.AddDividendPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.dividend.AddDividendResponseDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendsPortfolioResponseDto
import com.finanzas.dash.finanzas.dto.response.dividend.MessageDividendsPortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Dividend
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.repository.DividendRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

import com.finanzas.dash.finanzas.repository.DividendAnnouncementRepository
import com.finanzas.dash.finanzas.dto.response.dividend.DividendCalendarResponseDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendCalendarSummaryDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendCalendarEventDto
import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import java.math.BigDecimal
import java.time.YearMonth

@Service
class DividendService(
    private val dividendRepository: DividendRepository,
    private val portfolioRepository: PortfolioRepository,
    private val portfolioService: PortfolioService,
    private val dividendAnnouncementRepository: DividendAnnouncementRepository
) {
    fun getAllDividendsPorfolio(portfolioId: Long): DividendsPortfolioResponseDto {
        val dividends = dividendRepository.findByPortfolioPortfolioId(portfolioId).sortedByDescending { it.paidDate }
        val portfolio = portfolioRepository.findByPortfolioId(portfolioId) ?: throw GeneralRequestException(
            errors = listOf("Portfolio not found"),
            HttpStatus.NOT_FOUND
        )
        return DividendsPortfolioResponseDto(
            message = MessageDividendsPortfolioResponseDto(
                portfolio?.stock!!.toDto(),
                dividends.map { it.toDto() })
        )
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
                this.netValue = request.value!! * request.exchangeRate!! - request.tax
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

    @Transactional
    fun deleteDividend(dividendId: Long) {
        val dividend = dividendRepository.findById(dividendId).orElseThrow { 
            GeneralRequestException(listOf("Dividend not found"), HttpStatus.NOT_FOUND) 
        }
        val portfolio = dividend.portfolio!!
        portfolio.dividends.remove(dividend)
        dividendRepository.delete(dividend)
        portfolioService.updatePortfolioData(portfolio.portfolioId!!)
    }

    @Transactional
    fun editDividend(dividendId: Long, request: AddDividendPortfolioRequestDto): AddDividendResponseDto {
        val dividend = dividendRepository.findById(dividendId).orElseThrow {
            GeneralRequestException(listOf("Dividend not found"), HttpStatus.NOT_FOUND)
        }

        try {
            dividend.apply {
                this.dividendType = request.dividendType
                this.value = request.value
                this.modifiedAt = OffsetDateTime.now()
                val dateStr = request.paidDate!!
                this.paidDate = if (dateStr.contains("T")) {
                    java.time.OffsetDateTime.parse(dateStr)
                } else {
                    java.time.LocalDate.parse(dateStr).atStartOfDay().atOffset(java.time.ZoneOffset.UTC)
                }
                this.currencyCode = request.currencyCode
                this.tax = request.tax!!
                this.netValue = request.value!! * request.exchangeRate!! - request.tax
                this.exchangeRate = request.exchangeRate!!
            }

            val updatedDividend = dividendRepository.save(dividend)
            portfolioService.updatePortfolioData(dividend.portfolio!!.portfolioId!!)
            return AddDividendResponseDto(message = updatedDividend.toDto())

        } catch (ex: Exception) {
            System.err.println("Error al editar dividendo " + ex.message)
            throw GeneralRequestException(
                errors = listOf("Ocurrio un error al intentar editar el dividendo."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    fun getDividendCalendar(userId: Long, month: Int, year: Int): DividendCalendarResponseDto {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = YearMonth.of(year, month).atEndOfMonth()
        val startOffsetDate = startDate.atStartOfDay().atOffset(ZoneOffset.UTC)
        val endOffsetDate = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.UTC)

        val portfolios = portfolioRepository.findByUserUserId(userId)
        val tickers = portfolios.mapNotNull { it.stock?.symbol }.distinct()

        val paidDividends = dividendRepository.findByUserAndDateRange(userId, startOffsetDate, endOffsetDate)
        
        val announcements = if (tickers.isNotEmpty()) {
            dividendAnnouncementRepository.findByTickerInAndPayDateBetween(tickers, startDate, endDate)
        } else {
            emptyList()
        }

        val events = mutableListOf<DividendCalendarEventDto>()
        var paidMonth = BigDecimal.ZERO
        var expectedMonth = BigDecimal.ZERO
        var pendingMonth = BigDecimal.ZERO

        // Add paid dividends
        paidDividends.forEach { div ->
            val netValue = div.netValue ?: BigDecimal.ZERO
            paidMonth = paidMonth.add(netValue)
            events.add(
                DividendCalendarEventDto(
                    ticker = div.portfolio?.stock?.symbol ?: "",
                    company = div.portfolio?.stock?.name,
                    amount = netValue,
                    type = div.dividendType?.name ?: "UNKNOWN",
                    date = div.paidDate?.toLocalDate().toString(),
                    isPaid = true,
                    dividendId = div.dividendId
                )
            )
        }

        // Add announced dividends
        announcements.forEach { ann ->
            // Try to see if this announcement is already paid
            val isAlreadyPaid = paidDividends.any { paid ->
                paid.portfolio?.stock?.symbol == ann.ticker &&
                paid.paidDate?.toLocalDate() == ann.payDate
            }

            // Calculate approx amount based on holdings
            val portfolio = portfolios.find { it.stock?.symbol == ann.ticker }
            val quantity = portfolio?.totalQuantity ?: BigDecimal.ZERO
            
            var approxAmount = quantity.multiply(ann.amount)
            if (ann.type == DividendTypeEnum.cash) {
                approxAmount = approxAmount.multiply(BigDecimal("0.7")) // Approx 30% tax deduction
            }

            expectedMonth = expectedMonth.add(approxAmount)

            if (!isAlreadyPaid && quantity > BigDecimal.ZERO) {
                pendingMonth = pendingMonth.add(approxAmount)
                events.add(
                    DividendCalendarEventDto(
                        ticker = ann.ticker,
                        company = ann.company,
                        amount = approxAmount,
                        type = ann.type.name,
                        date = ann.payDate.toString(),
                        isPaid = false
                    )
                )
            }
        }

        return DividendCalendarResponseDto(
            events = events.sortedBy { it.date },
            summary = DividendCalendarSummaryDto(
                paidMonth = paidMonth,
                expectedMonth = expectedMonth,
                pendingMonth = pendingMonth
            )
        )
    }
}