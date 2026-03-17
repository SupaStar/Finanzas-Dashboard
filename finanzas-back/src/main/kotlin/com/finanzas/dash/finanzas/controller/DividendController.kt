package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.dividend.AddDividendPortfolioRequestDto
import com.finanzas.dash.finanzas.dto.response.dividend.AddDividendResponseDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendsPortfolioResponseDto
import com.finanzas.dash.finanzas.service.DividendService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dividend")
class DividendController(private val dividendService: DividendService) {
    @GetMapping("/all/{idPortfolio}")
    fun getAll(@PathVariable idPortfolio: Long): DividendsPortfolioResponseDto {
        return dividendService.getAllDividendsPorfolio(idPortfolio)
    }

    @PostMapping("/add/{idPortfolio}")
    fun add(
        @PathVariable idPortfolio: Long,
        @RequestBody @Valid request: AddDividendPortfolioRequestDto
    ): AddDividendResponseDto {
        return dividendService.addDividend(idPortfolio, request)
    }

    @DeleteMapping("/delete/{dividendId}")
    fun delete(@PathVariable dividendId: Long) {
        dividendService.deleteDividend(dividendId)
    }

    @org.springframework.web.bind.annotation.PutMapping("/edit/{dividendId}")
    fun edit(
        @PathVariable dividendId: Long,
        @RequestBody @Valid request: AddDividendPortfolioRequestDto
    ): AddDividendResponseDto {
        return dividendService.editDividend(dividendId, request)
    }
}