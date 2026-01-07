package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime


@Entity
@Table(name = "stock")
class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val stockId: Long? = null

    @Column(nullable = false, length = 150)
    var name: String? = null

    @Column(nullable = false, length = 50)
    var symbol: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    var broker: Broker? = null

    var closeDay: BigDecimal? = null
    var lastFetch: OffsetDateTime? = null

    @Column(nullable = false, length = 3)
    var currency: String? = null
}
