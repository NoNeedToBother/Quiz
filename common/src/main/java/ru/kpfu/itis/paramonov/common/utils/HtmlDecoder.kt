package ru.kpfu.itis.paramonov.common.utils

interface HtmlDecoder {
    fun decode(encoded: String): String
}