package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.stocks.AddStockRequestDto
import com.finanzas.dash.finanzas.dto.response.stock.StockGetAllResponseDto
import com.finanzas.dash.finanzas.dto.response.stock.StockResponseDto
import com.finanzas.dash.finanzas.service.StockService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockController(private val stockService: StockService) {

    @PostMapping("/add")
    fun addStock(@Validated @RequestBody requestDto: AddStockRequestDto): StockResponseDto {
        return stockService.addStock(requestDto)
    }

    @GetMapping("/all")
    fun getAll(): StockGetAllResponseDto {
        return stockService.getAll()
    }
}