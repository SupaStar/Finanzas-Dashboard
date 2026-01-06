package com.finanzas.dash.finanzas.config.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    val errors: List<String>,
    val status: HttpStatus
) : RuntimeException()
