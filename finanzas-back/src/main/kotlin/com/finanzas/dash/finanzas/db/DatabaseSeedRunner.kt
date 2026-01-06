package com.finanzas.dash.finanzas.db

import com.finanzas.dash.finanzas.db.seeder.DatabaseSeeder
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseSeedRunner(private val seeders: List<DatabaseSeeder>) {
    @Bean
    fun seedDatabase(): CommandLineRunner {
        return CommandLineRunner {
            seeders.forEach { it.seed() }
        }
    }
}