package com.frontend.finanzasdashfront.utils


fun formatMillisToDateString(millis: Long): String {
    if (millis == null) return "No date selected"

    // Calculate days since epoch (Jan 1, 1970)
    val daysSinceEpoch = millis / (1000 * 60 * 60 * 24)

    // Calculate year, month and day
    var remainingDays = daysSinceEpoch
    var year = 1970

    // Account for leap years
    while (true) {
        val daysInYear = if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) 366 else 365
        if (remainingDays < daysInYear) break
        remainingDays -= daysInYear
        year++
    }

    // Determine month and day
    val daysInMonth = arrayOf(31, if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    var month = 0

    while (month < 12) {
        if (remainingDays < daysInMonth[month]) break
        remainingDays -= daysInMonth[month]
        month++
    }

    val day = remainingDays.toInt() + 1
    month += 1  // Adjust month to be 1-based
    var monthString = "$month"
    if(month<10){
        monthString = "0$month"
    }
    var dayString = "$day"
    if(day<10){
        dayString = "0$day"
    }

    // Month names
    val monthNames = arrayOf("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")

    return "$year-${monthString}-$dayString"
}