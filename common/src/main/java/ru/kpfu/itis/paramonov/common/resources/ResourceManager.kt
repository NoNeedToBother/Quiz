package ru.kpfu.itis.paramonov.common.resources

import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable

interface ResourceManager {
    fun getString(stringId: Int): String

    fun getString(stringId: Int, vararg args: Any?): String

    fun getDrawable(drawableId: Int): Drawable

    fun getTheme(themeId: Int): Theme

    fun getColor(colorId: Int): Int
}