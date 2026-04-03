package com.finanzas.dash.finanzas.config

import com.finanzas.dash.finanzas.config.job.ActualizarPrecios
import com.finanzas.dash.finanzas.config.job.DailyFixedPortfolioInterestJob
import com.finanzas.dash.finanzas.config.job.PortfolioGeneralInformationJob
import com.finanzas.dash.finanzas.config.job.PortfolioMonthlyCapitalGainsJob
import com.finanzas.dash.finanzas.config.job.PortfolioHistoricalCapitalGainsJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.TimeZone

@Configuration
class QuartzConfig {

    @Bean
    fun jobDetail(): JobDetail {
        return JobBuilder.newJob(ActualizarPrecios::class.java)
            .withIdentity("tareaActualizarPrecios", "stock")
            .storeDurably() // Importante para que Spring lo maneje
            .build()
    }

    @Bean
    fun jobTrigger(jobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("triggerHorarioOficina", "stock")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 7-16 ? * MON-FRI")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City")))
            .build()
    }

    @Bean
    fun portfolioInfoJobDetail(): JobDetail {
        return JobBuilder.newJob(PortfolioGeneralInformationJob::class.java)
            .withIdentity("tareaCalculoInfoPortafolio", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun portfolioInfoJobTrigger(portfolioInfoJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(portfolioInfoJobDetail)
            .withIdentity("triggerCalculoDiario", "portfolio")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ?")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City"))) // 2:00 AM every day
            .build()
    }

    @Bean
    fun dailyInterestJobDetail(): JobDetail {
        return JobBuilder.newJob(DailyFixedPortfolioInterestJob::class.java)
            .withIdentity("tareaCalculoInteresDiario", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun dailyInterestJobTrigger(dailyInterestJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(dailyInterestJobDetail)
            .withIdentity("triggerCalculoInteresDiario", "portfolio")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 30 0 * * ?")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City"))) // 12:30 AM every day
            .build()
    }

    @Bean
    fun monthlyCapitalGainsJobDetail(): JobDetail {
        return JobBuilder.newJob(PortfolioMonthlyCapitalGainsJob::class.java)
            .withIdentity("tareaCalculoMensualPlusvalia", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun monthlyCapitalGainsJobTrigger(monthlyCapitalGainsJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(monthlyCapitalGainsJobDetail)
            .withIdentity("triggerCalculoMensualPlusvalia", "portfolio")
            // CRON TEMPORAL: Para pruebas ejecutará cada minuto. 
            // Vuelve a cambiarlo a "0 0 1 1 * ?" cuando pases a producción
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 1 * ?")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City"))) 
                .startNow()
            .build()
    }

    @Bean
    fun historicalCapitalGainsJobDetail(): JobDetail {
        return JobBuilder.newJob(PortfolioHistoricalCapitalGainsJob::class.java)
            .withIdentity("tareaCalculoHistoricoPlusvalia", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun historicalCapitalGainsJobTrigger(historicalCapitalGainsJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(historicalCapitalGainsJobDetail)
            .withIdentity("triggerCalculoHistoricoPlusvalia", "portfolio")
            // Temporarily every minute for testing, like the monthly one
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 1 * ?")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City"))) 
                .startNow()
            .build()
    }

    @Bean
    fun masDividendosSyncJobDetail(): JobDetail {
        return JobBuilder.newJob(com.finanzas.dash.finanzas.config.job.MasDividendosSyncJob::class.java)
            .withIdentity("tareaSincronizacionMasDividendos", "dividendos")
            .storeDurably()
            .build()
    }

    @Bean
    fun masDividendosSyncJobTrigger(masDividendosSyncJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(masDividendosSyncJobDetail)
            .withIdentity("triggerSincronizacionMasDividendos", "dividendos")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/2 * * ?")
                .inTimeZone(TimeZone.getTimeZone("America/Mexico_City")))
            .build()
    }
}