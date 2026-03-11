package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.config.exception.GeneralRequestException
import com.finanzas.dash.finanzas.dto.request.fixed_instrument.CreateFixedInstrumentRequestDto
import com.finanzas.dash.finanzas.dto.request.fixed_instrument.UpdateFixedInstrumentRequestDto
import com.finanzas.dash.finanzas.dto.response.fixed_instrument.FixedInstrumentResponseDto
import com.finanzas.dash.finanzas.entity.FixedInstrument
import com.finanzas.dash.finanzas.repository.FixedInstrumentRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FixedInstrumentService(
    private val fixedInstrumentRepository: FixedInstrumentRepository
) {

    fun getAllFixedInstruments(): List<FixedInstrumentResponseDto> {
        return fixedInstrumentRepository.findAll().map {
            FixedInstrumentResponseDto(
                id = it.id!!,
                name = it.name!!,
                anualRate = it.anualRate!!
            )
        }
    }

    @Transactional
    fun createFixedInstrument(dto: CreateFixedInstrumentRequestDto): FixedInstrumentResponseDto {
        val instrument = FixedInstrument().apply {
            this.name = dto.name
            this.anualRate = dto.anualRate
        }
        val saved = fixedInstrumentRepository.save(instrument)
        return FixedInstrumentResponseDto(
            id = saved.id!!,
            name = saved.name!!,
            anualRate = saved.anualRate!!
        )
    }

    @Transactional
    fun updateFixedInstrument(id: Long, dto: UpdateFixedInstrumentRequestDto): FixedInstrumentResponseDto {
        val instrument = fixedInstrumentRepository.findById(id)
            .orElseThrow { GeneralRequestException(listOf("FixedInstrument not found"), HttpStatus.NOT_FOUND) }

        dto.name?.let { instrument.name = it }
        dto.anualRate?.let { instrument.anualRate = it }

        val saved = fixedInstrumentRepository.save(instrument)
        return FixedInstrumentResponseDto(
            id = saved.id!!,
            name = saved.name!!,
            anualRate = saved.anualRate!!
        )
    }

    @Transactional
    fun deleteFixedInstrument(id: Long) {
        val instrument = fixedInstrumentRepository.findById(id)
            .orElseThrow { GeneralRequestException(listOf("FixedInstrument not found"), HttpStatus.NOT_FOUND) }
        fixedInstrumentRepository.delete(instrument)
    }
}
