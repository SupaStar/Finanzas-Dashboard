package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "fixed_instruments")
class FixedInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 150)
    var name: String? = null

    @Column(name = "anual_rate", nullable = false, precision = 15, scale = 6)
    var anualRate: BigDecimal? = null

    @OneToMany(mappedBy = "fixedInstrument", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var portfolios: MutableList<FixedPortfolio> = mutableListOf()
}
