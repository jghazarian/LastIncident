package com.jghazarian.lastincident.util

import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

fun convertMillisToDate(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val dateFormat = DateTimeComponents.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()

        char(' ')

        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    return instant.format(dateFormat)   //TODO: this can be given an offset, but due to off by one errors with timezones, this is probably what we want
}

//fun convertMillisToHours(millis: Long): String {
//
//}