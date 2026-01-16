package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.response.utils.UsdValueResponseDto
import com.finanzas.dash.finanzas.service.UtilService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/util")
class UtilController(private val utilService: UtilService) {
    @GetMapping("/usd-value")
    fun getUsdValue(): UsdValueResponseDto {
        return utilService.getUsdValue()
    }
}