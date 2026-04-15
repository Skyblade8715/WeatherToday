package com.skycom.weathertoday.presentation.details

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeFormatter {

    private val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dayHourFormatter = DateTimeFormatter.ofPattern("EEE HH:mm")

    fun formatHour(dateTime: LocalDateTime): String {
        return dateTime.format(hourFormatter)
    }

    fun formatForForecastRow(
        dateTime: LocalDateTime,
        now: LocalDateTime = LocalDateTime.now(),
    ): String {
        return if (dateTime.toLocalDate() == now.toLocalDate()) {
            dateTime.format(hourFormatter)
        } else {
            dateTime.format(dayHourFormatter)
        }
    }
}