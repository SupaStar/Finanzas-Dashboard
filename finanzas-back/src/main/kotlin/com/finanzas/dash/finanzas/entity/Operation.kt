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
    val operationId: Long? = null

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    var operationType: OperationTypeEnum? = OperationTypeEnum.buy // buy | sell

    @Column(nullable = false, precision = 15, scale = 6)
    var quantity: BigDecimal? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var price: BigDecimal? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    var portfolio: Portfolio? = null

    @Column(precision = 15, scale = 6)
    var fee: BigDecimal = BigDecimal.ZERO

    @Column(precision = 15, scale = 6)
    var tax: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false, precision = 15, scale = 6)
    var total: BigDecimal? = null

    val createdAt: OffsetDateTime = OffsetDateTime.now()
    val modifiedAt: OffsetDateTime? = null
}
