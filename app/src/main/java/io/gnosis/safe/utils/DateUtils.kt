package io.gnosis.safe.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun Date.formatBackendDate(zoneId : ZoneId = ZoneId.systemDefault(), locale: Locale = Locale.getDefault()): String {
    val customFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(zoneId).withLocale(locale)
    return customFormatter.format(Instant.ofEpochMilli(time))
}
