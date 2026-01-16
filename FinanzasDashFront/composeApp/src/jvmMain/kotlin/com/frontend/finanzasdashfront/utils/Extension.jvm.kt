package com.frontend.finanzasdashfront.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

actual fun Float.formatCurrency(): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    val formatter = DecimalFormat("#,##0.00", symbols)
    return formatter.format(this)
}