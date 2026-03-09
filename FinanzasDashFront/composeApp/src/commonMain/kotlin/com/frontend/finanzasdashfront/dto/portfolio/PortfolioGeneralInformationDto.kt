package com.frontend.finanzasdashfront.dto.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class PortfolioGeneralInformationDto(
    var portfolioGeneralInformationId: Long,
    var year: Int,
    var month: Int,
    var dividendsTotal: Float,
)
