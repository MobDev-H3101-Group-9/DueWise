package com.duewise.app.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val fullDate = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
private val dayHeader = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)

/** The one date format used on every screen (decision A5): "September 20, 2026". */
fun LocalDate.toDisplayDate(): String = format(fullDate)

/** Today's header line: "Thursday, September 17". */
fun LocalDate.toDayHeader(): String = format(dayHeader)

private const val MILLIS_PER_DAY = 86_400_000L

/** The Material date picker works in UTC milliseconds; these convert to and from LocalDate. */
fun LocalDate.toUtcMillis(): Long = toEpochDay() * MILLIS_PER_DAY

fun utcMillisToLocalDate(millis: Long): LocalDate = LocalDate.ofEpochDay(Math.floorDiv(millis, MILLIS_PER_DAY))
