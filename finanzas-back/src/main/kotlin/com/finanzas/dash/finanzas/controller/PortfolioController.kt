package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.portfolio.AddStockPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioGetAllResponseDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioResponseDto
import com.finanzas.dash.finanzas.service.PortfolioService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/portfolio")
class PortfolioController(private val portfolioService: PortfolioService) {
    @GetMapping("/test")
    fun test(){
        portfolioService.test()
    }

    @PostMapping("/add")
    fun add(@Validated @RequestBody requestDto: AddStockPortfolioRequestDto): PortfolioResponseDto {
        return portfolioService.addStockUserPortfolio(requestDto)
    }
    @GetMapping("/get")
    fun getPortfolio(): PortfolioGetAllResponseDto {
        return portfolioService.getUserPortfolio()
    }

    @GetMapping("/get/{id}")
    fun getSinglePortfolio(@PathVariable id: Long): PortfolioGetAllResponseDto {
        return portfolioService.getSingleUserPortfolio(id)
    }
}