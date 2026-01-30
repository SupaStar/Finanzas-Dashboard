package com.finanzas.dash.finanzas.controller

import com.finanzas.dash.finanzas.service.CsvService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/lotes")
class ProcesadorLotesController(private val csvService: CsvService) {
    @PostMapping("/operations")
    fun add(@RequestParam("file") file : MultipartFile) {
        if(file.isEmpty()){
            println("file is empty")
            //return ResponseEntity.badRequest().body("Archivo vacio")
        }
        csvService.processCsv(file)
        println("Archivo procesado")
        //return ResponseEntity.ok().body("Csv procesado con exito")
    }
    @PostMapping("/stocks")
    fun addStocks(@RequestParam("file") file : MultipartFile) {
        if(file.isEmpty()){
            println("file is empty")
        }
        csvService.addStocks(file)
        println("Archivo procesado")
    }
    @PostMapping("/dividends")
    fun dividend(@RequestParam("file") file : MultipartFile) {
        if(file.isEmpty()){
            println("file is empty")
        }
        csvService.addDividends(file)
        println("Archivo procesado")
    }

    @GetMapping("/update")
    fun update() {
        csvService.updateAllPortfolios()
    }

    @GetMapping("/updateDividends")
    fun updateDividends() {
        csvService.updateAllPortfoliosDividends()
    }
}