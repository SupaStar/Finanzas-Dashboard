package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(name = "portfolio", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = ["user_id", "stock_id"])))
class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val portfolioId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    var stock: Stock? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var avgPrice: BigDecimal? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var totalQuantity: BigDecimal? = null

    @OneToMany(mappedBy = "portfolio", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var dividends: MutableList<Dividend> = mutableListOf()
}
