package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.fixed_portfolio.CreateFixedPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.request.fixed_portfolio.UpdateFixedPortfolioAmountDto
import com.finanzas.dash.finanzas.dto.response.fixed_portfolio.FixedPortfolioResponseDto
import com.finanzas.dash.finanzas.service.FixedPortfolioService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fixed-portfolio")
class FixedPortfolioController(
    private val fixedPortfolioService: FixedPortfolioService
) {

    @GetMapping
    fun getAllByUser(): List<FixedPortfolioResponseDto> {
        return fixedPortfolioService.getAllFixedPortfoliosByUser()
    }

    @PostMapping
    fun create(@Validated @RequestBody dto: CreateFixedPortfolioRequestDto): FixedPortfolioResponseDto {
        return fixedPortfolioService.createFixedPortfolio(dto)
    }

    @PutMapping("/{id}")
    fun updateAmount(@PathVariable id: Long, @Validated @RequestBody dto: UpdateFixedPortfolioAmountDto): FixedPortfolioResponseDto {
        return fixedPortfolioService.updateFixedPortfolioAmount(id, dto)
    }

    @DeleteMapping("/{id}")
    fun deleteLogically(@PathVariable id: Long) {
        fixedPortfolioService.deleteFixedPortfolioLogically(id)
    }
}
