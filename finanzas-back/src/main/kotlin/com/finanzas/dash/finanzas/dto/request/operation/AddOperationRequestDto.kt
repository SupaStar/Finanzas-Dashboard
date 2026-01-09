package com.finanzas.dash.finanzas.dto.request.operation

import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AddOperationRequestDto (
    @field:NotNull(message = "El tipo de operacion no puede estar vacio")
    val operationType: OperationTypeEnum?,
    @field:NotNull(message = "La cantidad no puede estar vacia")
    val quantity: BigDecimal?,
    @field:NotNull(message = "El precio no puede estar vacio")
    val price: BigDecimal?,
    @field:NotNull(message = "La comision no puede estar vacia")
    val fee: BigDecimal?,
    @field:NotNull(message = "El impuesto no puede estar vacio")
    val tax: BigDecimal?,
    @field:NotNull(message = "El campo portafolio no puede estar vacio")
    val portfolioId: Long?,
    @field:NotNull(message = "La fecha de operacion no puede estar vacia")
    val operationDate: String?,
)