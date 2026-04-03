package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.MasDividendosService
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.OffsetDateTime

class MasDividendosSyncJob(private val masDividendosService: MasDividendosService) : Job {
    override fun execute(context: JobExecutionContext?) {
        System.out.println("Ejecutando sincronización de MasDividendos " + OffsetDateTime.now())
        masDividendosService.syncDividends()
        System.out.println("Fin sincronización de MasDividendos " + OffsetDateTime.now())
    }
}
