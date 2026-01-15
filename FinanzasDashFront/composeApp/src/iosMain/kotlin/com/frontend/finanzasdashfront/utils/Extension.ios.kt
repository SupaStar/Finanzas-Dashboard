package com.frontend.finanzasdashfront.utils

import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Float.formatCurrency(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        groupingSeparator = ","
        decimalSeparator = "."
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    return formatter.stringFromNumber(NSNumber(this)) ?: ""
}