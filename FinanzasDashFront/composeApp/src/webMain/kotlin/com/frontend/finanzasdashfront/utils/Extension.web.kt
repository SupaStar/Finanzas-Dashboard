package com.frontend.finanzasdashfront.utils

import kotlin.js.js

// webMain
actual fun Float.formatCurrency(): String =
    js(
        "Number(this).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })"
    ) as String
