package com.finanzas.dash.finanzas.config.exception

import com.finanzas.dash.finanzas.dto.GeneralExceptionResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<GeneralExceptionResponseDto<String?>> {
        val errors = ex.bindingResult.fieldErrors.map { it.defaultMessage }
        val response = GeneralExceptionResponseDto(errors = errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<GeneralExceptionResponseDto<String>> {
        val errors = listOf("El cuerpo de la petición no puede estar vacío o es inválido")
        val response = GeneralExceptionResponseDto(errors = errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException):ResponseEntity<GeneralExceptionResponseDto<String>>{
        val response = GeneralExceptionResponseDto(
            errors = ex.errors
        )
        return ResponseEntity(response, ex.status)
    }
}