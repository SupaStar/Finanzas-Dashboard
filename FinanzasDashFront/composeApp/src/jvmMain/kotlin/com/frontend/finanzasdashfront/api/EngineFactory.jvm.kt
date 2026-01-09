package com.frontend.finanzasdashfront.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.apache5.Apache5

actual fun getEngine(): HttpClientEngine = Apache5.create()