package com.frontend.finanzasdashfront.utils

import com.frontend.finanzasdashfront.dto.dividend.DividendDto
import com.frontend.finanzasdashfront.dto.enums.DividendTypeEnum
import com.frontend.finanzasdashfront.dto.enums.OperationTypeEnum
import com.frontend.finanzasdashfront.dto.operation.OperationDto

expect fun Float.formatCurrency(): String


fun OperationTypeEnum.toLabel(): String {
    if (this == OperationTypeEnum.buy) {
        return "Compra"
    } else {
        return "Venta"
    }
}

fun DividendTypeEnum.toLabel(): String {
    if(this == DividendTypeEnum.cash){
        return "Dividendo"
    }else{
        return "Reembolso"
    }
}

fun DividendDto.year(): Int =
    paidDate.substring(0, 4).toInt()

fun DividendDto.month(): Int =
    paidDate.substring(5, 7).toInt()

fun OperationDto.year(): Int =
    operationDate.substring(0, 4).toInt()

fun OperationDto.month(): Int =
    operationDate.substring(5, 7).toInt()