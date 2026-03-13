package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "daily_pays")
class DailyPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_portfolio_id", nullable = false)
    var fixedPortfolio: FixedPortfolio? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var amount: BigDecimal? = null

    @Column(name = "anual_rate_calculated", nullable = false, precision = 15, scale = 6)
    var anualRateCalculated: BigDecimal? = null

    @Column(name = "pay_date", nullable = false)
    var payDate: LocalDate = LocalDate.now()
}
