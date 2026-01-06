package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(name = "portfolio", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = ["user_id", "stock_id"])))
class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val portfolioId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private val stock: Stock? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private val user: User? = null

    @Column(nullable = false, precision = 15, scale = 6)
    private var avgPrice: BigDecimal? = null

    @Column(nullable = false, precision = 15, scale = 6)
    private var totalQuantity: BigDecimal? = null
}
