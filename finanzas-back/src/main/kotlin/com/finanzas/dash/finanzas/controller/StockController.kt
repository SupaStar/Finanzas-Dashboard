package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.stocks.AddStockRequestDto
import com.finanzas.dash.finanzas.dto.request.stocks.StockInfoRequestDto
import com.finanzas.dash.finanzas.dto.response.stock.StockResponseDto
import com.finanzas.dash.finanzas.service.StockApiService
import com.finanzas.dash.finanzas.service.StockService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockController(private val stockApiService: StockApiService, private val stockService: StockService) {
    @GetMapping("/{stock}")
    fun getStock(@PathVariable stock: String) {
        val owo = stockApiService.getStock("FMTY14.MX")
        print(stock)
    }

    @PostMapping("/")
    fun getStocks(@Validated @RequestBody requestDto: StockInfoRequestDto) {
        val owo = stockApiService.getStocks(requestDto)
        print(requestDto)
    }

    @PostMapping("/add")
    fun addStock(@Validated @RequestBody requestDto: AddStockRequestDto): StockResponseDto {
        return stockService.addStock(requestDto)
    }
}