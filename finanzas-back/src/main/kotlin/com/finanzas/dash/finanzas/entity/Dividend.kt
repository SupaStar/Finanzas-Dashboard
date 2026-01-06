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
    private val dividendId: Long? = null

    @Column(nullable = false, length = 20)
    private var dividendType: DividendTypeEnum? = DividendTypeEnum.CASH // cash | stock

    @Column(nullable = false, precision = 15, scale = 6)
    private var value: BigDecimal? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private val portfolio: Portfolio? = null

    private val createdAt: OffsetDateTime = OffsetDateTime.now()
    private val modifiedAt: OffsetDateTime? = null

    private val paidDate: LocalDate? = null

    @Column(nullable = false, length = 3)
    private var currencyCode: String? = null

    @Column(precision = 15, scale = 6)
    private var tax: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false, precision = 15, scale = 6)
    private var netValue: BigDecimal? = null

    @Column(precision = 15, scale = 6)
    private var exchangeRate: BigDecimal? = null
}
