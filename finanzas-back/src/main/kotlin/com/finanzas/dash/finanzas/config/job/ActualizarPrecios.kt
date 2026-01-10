package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.StockService
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.OffsetDateTime

class ActualizarPrecios(private val stockService: StockService) : Job {
    override fun execute(context: JobExecutionContext?) {
        System.out.println("Ejecutando actualizacion de precios " + OffsetDateTime.now())
        stockService.refreshAllStocks()
        System.out.println("Fin  actualizacion de precios " + OffsetDateTime.now())
    }

}