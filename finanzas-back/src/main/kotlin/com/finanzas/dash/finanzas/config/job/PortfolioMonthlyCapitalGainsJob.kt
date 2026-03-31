package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.PortfolioMonthlyCapitalGainsService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class PortfolioMonthlyCapitalGainsJob(private val service: PortfolioMonthlyCapitalGainsService) : Job {
    private val log = LoggerFactory.getLogger(PortfolioMonthlyCapitalGainsJob::class.java)

    override fun execute(context: JobExecutionContext?) {
        log.info("Job disparado: Calculo mensual de plusvalia de portafolios.")
        service.calculateMonthlyCapitalGains()
        log.info("Job finalizado: Calculo mensual de plusvalia de portafolios.")
    }
}
