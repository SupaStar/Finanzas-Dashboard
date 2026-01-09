package com.frontend.finanzasdashfront.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun getEngine(): HttpClientEngine = Darwin.create()