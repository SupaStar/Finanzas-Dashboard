package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.dto.request.fixed_instrument.CreateFixedInstrumentRequestDto
import com.finanzas.dash.finanzas.dto.request.fixed_instrument.UpdateFixedInstrumentRequestDto
import com.finanzas.dash.finanzas.dto.response.fixed_instrument.FixedInstrumentResponseDto
import com.finanzas.dash.finanzas.service.FixedInstrumentService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fixed-instrument")
class FixedInstrumentController(
    private val fixedInstrumentService: FixedInstrumentService
) {

    @GetMapping
    fun getAll(): List<FixedInstrumentResponseDto> {
        return fixedInstrumentService.getAllFixedInstruments()
    }

    @PostMapping
    fun create(@Validated @RequestBody dto: CreateFixedInstrumentRequestDto): FixedInstrumentResponseDto {
        return fixedInstrumentService.createFixedInstrument(dto)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Validated @RequestBody dto: UpdateFixedInstrumentRequestDto): FixedInstrumentResponseDto {
        return fixedInstrumentService.updateFixedInstrument(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        fixedInstrumentService.deleteFixedInstrument(id)
    }
}
