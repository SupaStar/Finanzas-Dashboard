package com.frontend.finanzasdashfront.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual fun getEngine(): HttpClientEngine = Js.create()