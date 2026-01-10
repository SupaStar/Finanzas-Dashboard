package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.operation.AddOperationRequestDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationAddResponseDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationsPortfolioResponseDto
import com.finanzas.dash.finanzas.service.OperationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/operation")
class OperationController(private val operationService: OperationService) {
    @GetMapping("/all/{portfolioId}")
    fun getAllOperations(@PathVariable portfolioId: Long): OperationsPortfolioResponseDto {
        return operationService.getOperationsPortfolio(portfolioId)
    }

    @PostMapping("/add")
    fun addOperation(@RequestBody @Valid requestDto: AddOperationRequestDto): OperationAddResponseDto {
        return operationService.addOperation(requestDto)
    }
}