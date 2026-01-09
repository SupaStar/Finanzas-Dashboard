package com.finanzas.dash.finanzas.dto.response.operation

import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import java.math.BigDecimal

data class OperationDto (
    val operationId: Long,
    val operationType: OperationTypeEnum,
    val quantity: BigDecimal,
    val price: BigDecimal,
    val fee: BigDecimal,
    val tax: BigDecimal,
    val total: BigDecimal,
)