package ru.kpfu.itis.paramonov.common.utils

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringId: Int): String

    fun getString(@StringRes stringId: Int, vararg args: Any?): String
}