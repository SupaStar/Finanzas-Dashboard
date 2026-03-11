package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "fixed_portfolio")
class FixedPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_instrument_id", nullable = false)
    var fixedInstrument: FixedInstrument? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var amount: BigDecimal? = null

    @Column(nullable = false)
    var deleted: Boolean = false

    @OneToMany(mappedBy = "fixedPortfolio", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var dailyPays: MutableList<DailyPay> = mutableListOf()
}
