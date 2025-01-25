package ru.kpfu.itis.paramonov.core.utils

interface HtmlDecoder {
    fun decode(encoded: String): String
}