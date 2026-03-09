package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.service.PortfolioGeneralInformationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jobs")
class JobController(private val portfolioGeneralInformationService: PortfolioGeneralInformationService) {

    @PostMapping("/portfolio-general-info")
    fun triggerPortfolioGeneralInfo(): ResponseEntity<String> {
        portfolioGeneralInformationService.calculateAndSaveGeneralInformation()
        return ResponseEntity.ok("Calculo de informacion general de portafolios ejecutado con exito")
    }
}
