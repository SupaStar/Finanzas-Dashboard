package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "portfolio_general_information")
class PortfolioGeneralInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val portfolioGeneralInformationId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    var portfolio: Portfolio? = null

    @Column(nullable = false)
    var year: Int? = null

    @Column(nullable = false)
    var month: Int? = null

    @Column(precision = 15, scale = 6)
    var dividendsTotal: BigDecimal? = BigDecimal.ZERO
}
