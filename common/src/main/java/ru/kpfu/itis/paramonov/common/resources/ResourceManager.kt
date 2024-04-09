package ru.kpfu.itis.paramonov.common.resources

import android.graphics.drawable.Drawable

interface ResourceManager {
    fun getString(stringId: Int): String

    fun getString(stringId: Int, vararg args: Any?): String

    fun getDrawable(drawableId: Int): Drawable
}