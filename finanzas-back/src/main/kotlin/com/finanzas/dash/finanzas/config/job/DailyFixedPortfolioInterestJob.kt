package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.FixedPortfolioService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class DailyFixedPortfolioInterestJob(private val service: FixedPortfolioService) : Job {
    private val log = LoggerFactory.getLogger(DailyFixedPortfolioInterestJob::class.java)

    override fun execute(context: JobExecutionContext?) {
        log.info("Job disparado: Calculo de interes diario de portafolios fijos.")
        service.calculateDailyInterest()
        log.info("Job finalizado: Calculo de interes diario de portafolios fijos.")
    }
}
