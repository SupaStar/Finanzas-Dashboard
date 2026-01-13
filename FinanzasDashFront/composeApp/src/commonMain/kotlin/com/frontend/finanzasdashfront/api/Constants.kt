package com.frontend.finanzasdashfront.api

import com.frontend.finanzasdashfront.getPlatformHost

object Constants {
    //const val BaseUrl: String = "http://localhost:8080"
    //Android
    //const val BaseUrl: String = "http://10.0.2.2:8080"
    val BaseUrl: String = getPlatformHost()
}