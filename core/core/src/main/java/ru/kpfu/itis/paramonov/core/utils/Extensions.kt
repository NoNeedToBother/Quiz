package ru.kpfu.itis.paramonov.core.utils

fun String.normalizeEnumName(): String {
    return this
        .lowercase()
        .replace("_", " ")
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase()
            else it.toString()
        }
}

fun String.toEnumName(): String {
    return this
        .uppercase()
        .replace(" ", "_")
}
