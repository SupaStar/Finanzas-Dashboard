package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime


@Entity
@Table(name = "stock")
class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val stockId: Long? = null

    @Column(nullable = false, length = 150)
    private var name: String? = null

    @Column(nullable = false, length = 50)
    private var symbol: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    private val broker: Broker? = null

    private val closeDay: BigDecimal? = null
    private val lastFetch: OffsetDateTime? = null

    @Column(nullable = false, length = 3)
    private var currency: String? = null
}
