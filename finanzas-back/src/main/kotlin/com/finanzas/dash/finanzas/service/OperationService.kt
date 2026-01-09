package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.operation.AddOperationRequestDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationsPortfolioResponseDto
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.repository.OperationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class OperationService(
    private val operationRepository: OperationRepository,
    private val portfolioRepository: PortfolioRepository
) {
    fun getOperationsPortfolio(portfolioId: Long): OperationsPortfolioResponseDto {
        val operations = operationRepository.findByPortfolioPortfolioId(portfolioId)
        return OperationsPortfolioResponseDto(message = operations.map { it.toDto() })
    }

    @Transactional
    fun addOperation(requestDto: AddOperationRequestDto) {
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
                this.total = total
            })
        } catch (e: Exception) {
            throw GeneralRequestException(
                errors = listOf("Ocurrio un error al intentar guardar la operacion."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}