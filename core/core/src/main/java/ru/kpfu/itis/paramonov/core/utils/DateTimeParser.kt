package ru.kpfu.itis.paramonov.core.utils

import java.util.Locale

interface DateTimeParser {

    fun parseString(time: String): DateTime

    fun parseString(time: String, dateFormat: String, locale: Locale): DateTime

    fun parseMillis(time: Long): DateTime

    fun parseMillisToString(time: Long): String

    fun parseMillisToString(time: Long, dateFormat: String, locale: Locale): String
}