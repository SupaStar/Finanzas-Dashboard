package com.frontend.finanzasdashfront.dto.operation

import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum
import kotlinx.serialization.Serializable

@Serializable
data class OperationDto (
    val operationId: Long,
    val operationType: OperationTypeEnum,
    val quantity: Float,
    val price: Float,
    val fee: Float,
    val tax: Float,
    val total: Float,
    val operationDate: String,
)