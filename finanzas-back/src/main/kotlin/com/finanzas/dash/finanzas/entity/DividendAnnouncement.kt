package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "dividend_announcements")
data class DividendAnnouncement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val sourceId: Long,
    val ticker: String,
    val company: String?,
    val amount: BigDecimal,
    val comment: String?,
    
    @Enumerated(EnumType.STRING)
    val type: DividendTypeEnum,
    
    val payDate: LocalDate?,
    val exDate: LocalDate?,
    val link: String?
)
