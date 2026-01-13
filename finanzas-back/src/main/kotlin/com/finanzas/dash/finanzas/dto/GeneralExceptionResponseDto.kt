package com.finanzas.dash.finanzas.dto

data class GeneralExceptionResponseDto<T>(
    val estado: Boolean = false,
    val errors: List<T> = emptyList()
)