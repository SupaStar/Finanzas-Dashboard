package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.DividendAnnouncement
import org.springframework.data.jpa.repository.JpaRepository

interface DividendAnnouncementRepository : JpaRepository<DividendAnnouncement, Int>
