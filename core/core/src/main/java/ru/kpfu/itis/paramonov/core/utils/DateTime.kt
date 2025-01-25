package ru.kpfu.itis.paramonov.core.utils

data class DateTime(
    val sec: Int,
    val min: Int,
    val hours: Int,
    val day: Int,
    val month: Int,
    val year: Int
): Comparable<DateTime> {
    override fun compareTo(other: DateTime): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        if (day != other.day) return day.compareTo(other.day)
        if (hours != other.hours) return hours.compareTo(other.hours)
        if (min != other.min) return min.compareTo(other.min)
        if (sec != other.sec) return sec.compareTo(other.sec)
        return 0
    }

}