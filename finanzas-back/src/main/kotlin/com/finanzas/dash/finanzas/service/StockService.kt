package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.stocks.AddStockRequestDto
import com.finanzas.dash.finanzas.dto.request.stocks.StockInfoRequestDto
import com.finanzas.dash.finanzas.dto.response.stock.StockResponseDto
import com.finanzas.dash.finanzas.entity.Stock
import com.finanzas.dash.finanzas.repository.BrokerRepository
import com.finanzas.dash.finanzas.repository.StockRepository
import com.finanzas.dash.finanzas.utils.extension.toDto
import org.apache.coyote.BadRequestException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class StockService(
    private val stockRepository: StockRepository,
    private val brokerRepository: BrokerRepository,
    private val stockApiService: StockApiService
) {

    fun addStock(requestDto: AddStockRequestDto): StockResponseDto {
        val broker = brokerRepository.findByBrokerId(requestDto.brokerId) ?: throw GeneralRequestException(
            listOf("Error al encontrar tu broker."),
            HttpStatus.CONFLICT
        )
        val stockName = "${requestDto.stockName}.${broker.symbol}"
        val stockInfo = stockApiService.getStock(stockName)
        val stock = try {
            stockRepository.save(Stock().apply {
                this.name = stockName
                this.symbol = requestDto.stockName
                this.broker = broker
                this.closeDay = stockInfo.data.price
                this.lastFetch = OffsetDateTime.now()
                this.currency = stockInfo.data.currency
            })
        } catch (ex: DataIntegrityViolationException) {
            throw GeneralRequestException(listOf("Accion ya registrada"), HttpStatus.CONFLICT)
        }
        return StockResponseDto(estado = true, message = stock.toDto())
    }

    fun refreshAllStocks() {
        val now = OffsetDateTime.now()

        val stocks = stockRepository.findAll()
        val stocksByName = stocks.associateBy { it.name!! }

        val stocksNames = StockInfoRequestDto(
            stocks = stocks.map { it.name!! }
        )

        val infoStocks = stockApiService.getStocks(stocksNames)

        infoStocks.forEach { response ->
            val stock = stocksByName[response.data.symbol] ?: return@forEach

            val expired = stock.lastFetch
                ?.isBefore(now.minusMinutes(2))
                ?: true

            if (expired) {
                stock.closeDay = response.data.price
                stock.lastFetch = now
                stockRepository.save(stock)
            }
        }
    }
}