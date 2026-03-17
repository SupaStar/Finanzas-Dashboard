package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.response.daily_pay.DailyPayResponseDto
import com.finanzas.dash.finanzas.service.DailyPayService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/daily-pay")
class DailyPayController(
    private val dailyPayService: DailyPayService
) {

    @GetMapping("/portfolio/{portfolioId}")
    fun getByPortfolio(@PathVariable portfolioId: Long): List<DailyPayResponseDto> {
        return dailyPayService.getDailyPaysByPortfolio(portfolioId)
    }
}
