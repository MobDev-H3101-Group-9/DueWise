package com.duewise.app.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// The one date format used on every screen: "September 20, 2026"
fun LocalDate.toDisplayDate(): String =
    format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH))

// Header on the Today screen: "Thursday, September 17"
fun LocalDate.toDayHeader(): String =
    format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH))
