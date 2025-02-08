package ru.kpfu.itis.paramonov.quiz.utils

import ru.kpfu.itis.paramonov.core.utils.DateTime
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeParserImpl: DateTimeParser {
    override fun parseString(time: String): DateTime {
        return parseString(time, DEFAULT_DATE_FORMAT, Locale.UK)
    }

    override fun parseString(time: String, dateFormat: String, locale: Locale): DateTime {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(dateFormat, locale)
        simpleDateFormat.parse(time)?.let {
            calendar.time = it
        }
        return fromCalendar(calendar)
    }

    override fun parseMillis(time: Long): DateTime {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return fromCalendar(calendar)
    }

    override fun parseMillisToString(time: Long): String {
        return parseMillisToString(time, DEFAULT_DATE_FORMAT, Locale.UK)
    }

    override fun parseMillisToString(time: Long, dateFormat: String, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat(dateFormat, locale)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return simpleDateFormat.format(calendar.time)
    }

    private fun fromCalendar(calendar: Calendar): DateTime {
        return DateTime(
            sec = calendar.get(Calendar.SECOND),
            min = calendar.get(Calendar.MINUTE),
            hours = calendar.get(Calendar.HOUR_OF_DAY),
            day = calendar.get(Calendar.DAY_OF_MONTH),
            month = calendar.get(Calendar.MONTH),
            year = calendar.get(Calendar.YEAR)
        )
    }

    companion object {
        const val DEFAULT_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss"
    }
}
