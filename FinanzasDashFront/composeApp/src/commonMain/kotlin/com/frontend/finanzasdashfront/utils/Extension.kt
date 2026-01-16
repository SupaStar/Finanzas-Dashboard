package com.frontend.finanzasdashfront.utils

import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum

expect fun Float.formatCurrency(): String


fun OperationTypeEnum.toLabel(): String {
    if (this == OperationTypeEnum.buy) {
        return "Compra"
    } else {
        return "Venta"
    }
}