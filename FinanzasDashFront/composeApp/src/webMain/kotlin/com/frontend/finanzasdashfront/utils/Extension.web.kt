package com.frontend.finanzasdashfront.utils

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js


@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""
    (value) => Number(value).toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })
""")
external fun formatCurrencyJs(value: Float): String


actual fun Float.formatCurrency(): String =
    formatCurrencyJs(this)
