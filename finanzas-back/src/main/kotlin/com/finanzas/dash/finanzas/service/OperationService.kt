package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.operation.AddOperationRequestDto
import com.finanzas.dash.finanzas.dto.response.operation.MessageOperationResponseDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationAddResponseDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationsPortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.repository.OperationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneOffset

@Service
class OperationService(
    private val operationRepository: OperationRepository,
    private val portfolioRepository: PortfolioRepository,
    private val portfolioService: PortfolioService,
) {
    fun getOperationsPortfolio(portfolioId: Long): OperationsPortfolioResponseDto {
        val operations = operationRepository.findByPortfolioPortfolioId(portfolioId).sortedByDescending { it.operationDate }
        val portfolio = portfolioRepository.findByPortfolioId(portfolioId)
        return OperationsPortfolioResponseDto(
            message = MessageOperationResponseDto(
                portfolio?.stock!!.toDto(),
                operations.map { it.toDto() })
        )
    }

    @Transactional
    fun addOperation(requestDto: AddOperationRequestDto): OperationAddResponseDto {
        val totalNet = requestDto.price!! * requestDto.quantity!!
        val total = totalNet + requestDto.tax!! + requestDto.fee!!
        val portfolio =
            portfolioRepository.findByPortfolioId(requestDto.portfolioId!!) ?: throw GeneralRequestException(
                errors = listOf("Portfolio not found"),
                HttpStatus.NOT_FOUND
            )
        try {
            val operation = operationRepository.save(Operation().apply {
                this.operationType = requestDto.operationType
                this.quantity = requestDto.quantity
                this.price = requestDto.price
                this.portfolio = portfolio
                this.fee = requestDto.fee
                this.tax = requestDto.tax
                this.total = totalNet
                this.operationDate = LocalDate.parse(requestDto.operationDate!!).atStartOfDay().atOffset(ZoneOffset.UTC)
            })
            portfolioService.updatePortfolioData(portfolio.portfolioId!!)
            return OperationAddResponseDto(message = operation.toDto())
        } catch (e: Exception) {
            System.err.println("Error al guardar operation " + e.message)
            throw GeneralRequestException(
                errors = listOf("Ocurrio un error al intentar guardar la operacion."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}