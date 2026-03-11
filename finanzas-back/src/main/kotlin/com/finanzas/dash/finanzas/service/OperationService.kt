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

    @Transactional
    fun deleteOperation(operationId: Long) {
        val operation = operationRepository.findById(operationId).orElseThrow { 
            GeneralRequestException(listOf("Operation not found"), HttpStatus.NOT_FOUND) 
        }
        val portfolio = operation.portfolio!!
        portfolio.operations.remove(operation)
        operationRepository.delete(operation)
        portfolioService.updatePortfolioData(portfolio.portfolioId!!)
    }

    @Transactional
    fun editOperation(operationId: Long, requestDto: AddOperationRequestDto): OperationAddResponseDto {
        val operation = operationRepository.findById(operationId).orElseThrow {
            GeneralRequestException(listOf("Operation not found"), HttpStatus.NOT_FOUND)
        }
        
        val totalNet = requestDto.price!! * requestDto.quantity!!
        val total = totalNet + requestDto.tax!! + requestDto.fee!!
        
        try {
            operation.apply {
                this.operationType = requestDto.operationType
                this.quantity = requestDto.quantity
                this.price = requestDto.price
                this.fee = requestDto.fee
                this.tax = requestDto.tax
                this.total = totalNet
                val dateStr = requestDto.operationDate!!
                this.operationDate = if (dateStr.contains("T")) {
                    java.time.OffsetDateTime.parse(dateStr)
                } else {
                    java.time.LocalDate.parse(dateStr).atStartOfDay().atOffset(java.time.ZoneOffset.UTC)
                }
            }
            
            val updatedOperation = operationRepository.save(operation)
            portfolioService.updatePortfolioData(operation.portfolio!!.portfolioId!!)
            return OperationAddResponseDto(message = updatedOperation.toDto())
            
        } catch (e: Exception) {
            System.err.println("Error al editar operation " + e.message)
            throw GeneralRequestException(
                errors = listOf("Ocurrio un error al intentar editar la operacion."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}