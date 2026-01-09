package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime


@Entity
@Table(name = "dividend")
class Dividend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val dividendId: Long? = null

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var dividendType: DividendTypeEnum? = DividendTypeEnum.cash // cash | stock

    @Column(nullable = false, precision = 15, scale = 6)
    var value: BigDecimal? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    val portfolio: Portfolio? = null

    val createdAt: OffsetDateTime = OffsetDateTime.now()
    val modifiedAt: OffsetDateTime? = null

    val paidDate: LocalDate? = null

    @Column(nullable = false, length = 3)
    var currencyCode: String? = null

    @Column(precision = 15, scale = 6)
    var tax: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false, precision = 15, scale = 6)
    var netValue: BigDecimal? = null

    @Column(precision = 15, scale = 6)
    var exchangeRate: BigDecimal? = null
}
