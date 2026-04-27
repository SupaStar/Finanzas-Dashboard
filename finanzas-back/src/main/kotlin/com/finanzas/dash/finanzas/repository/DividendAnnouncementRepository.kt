package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.DividendAnnouncement
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DividendAnnouncementRepository : JpaRepository<DividendAnnouncement, Int> {
    fun findByTickerInAndPayDateBetween(tickers: List<String>, startDate: LocalDate, endDate: LocalDate): List<DividendAnnouncement>
}
