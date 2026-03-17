package com.finanzas.dash.finanzas.dto.response.portfolio

import java.math.BigDecimal

data class PortfolioGeneralInformationDto(
    var portfolioGeneralInformationId: Long,
    var year: Int,
    var month: Int,
    var dividendsTotal: BigDecimal,
)
