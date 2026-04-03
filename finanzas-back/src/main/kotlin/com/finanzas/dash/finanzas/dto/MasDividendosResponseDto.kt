package com.finanzas.dash.finanzas.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MasDividendosResponseDto(
    val id: String?,
    val empresa: String?,
    val ticker: String?,
    val monto: String?,
    val comentario: String?,
    @JsonProperty("fecha_pago") val fechaPago: String?,
    @JsonProperty("fecha_ex_derecho") val fechaExDerecho: String?,
    @JsonProperty("link_aviso") val linkAviso: String?
)
