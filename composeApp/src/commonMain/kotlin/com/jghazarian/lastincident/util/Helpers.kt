package com.jghazarian.lastincident.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun convertMillisToDate(millis: Long, showSeconds: Boolean = false): String {
    val instant = Instant.fromEpochMilliseconds(millis)

    val dateFormat = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()

        char(' ')

        amPmHour()
        char(':')
        minute()
        if (showSeconds) {
            char(':')
            second()
        }
        char(' ')
        amPmMarker("AM", "PM")
    }

    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).format(dateFormat)
}