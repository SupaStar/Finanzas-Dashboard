package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.FixedPortfolioOperationTypeEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "fixed_portfolio_operation")
class FixedPortfolioOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_portfolio_id", nullable = false)
    var fixedPortfolio: FixedPortfolio? = null

    @Column(nullable = false, precision = 15, scale = 6)
    var amount: BigDecimal? = null

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var operationType: FixedPortfolioOperationTypeEnum? = null

    @Column(nullable = false)
    var operationDate: OffsetDateTime = OffsetDateTime.now()
}
