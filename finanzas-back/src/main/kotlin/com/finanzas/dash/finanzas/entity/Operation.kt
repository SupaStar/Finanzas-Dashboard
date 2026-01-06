package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime


@Entity
@Table(name = "operation")
class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val operationId: Long? = null

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private var operationType: OperationTypeEnum? = OperationTypeEnum.buy // buy | sell

    @Column(nullable = false, precision = 15, scale = 6)
    private var quantity: BigDecimal? = null

    @Column(nullable = false, precision = 15, scale = 6)
    private var price: BigDecimal? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private val portfolio: Portfolio? = null

    @Column(precision = 15, scale = 6)
    private var fee: BigDecimal = BigDecimal.ZERO

    @Column(precision = 15, scale = 6)
    private var tax: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false, precision = 15, scale = 6)
    private var total: BigDecimal? = null

    private val createdAt: OffsetDateTime = OffsetDateTime.now()
    private val modifiedAt: OffsetDateTime? = null
}
