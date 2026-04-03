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
    val id: Int = 0,

    val sourceId: Long = 0L,
    val ticker: String = "",
    val company: String? = null,
    val amount: BigDecimal = BigDecimal.ZERO,
    val comment: String? = null,
    
    @Enumerated(EnumType.STRING)
    val type: DividendTypeEnum = DividendTypeEnum.UNDEFINED,
    
    val payDate: LocalDate? = null,
    val exDate: LocalDate? = null,
    val link: String? = null
)
