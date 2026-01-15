package com.frontend.finanzasdashfront.utils

import kotlin.js.js


// webMain
actual fun Float.formatCurrency(): String =
    formatCurrencyJs(this)

external fun formatCurrencyJs(value: Float): String
