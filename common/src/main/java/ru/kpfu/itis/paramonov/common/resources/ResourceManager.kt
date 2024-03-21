package ru.kpfu.itis.paramonov.common.resources

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringId: Int): String

    fun getString(@StringRes stringId: Int, vararg args: Any?): String

    fun getDrawable(@DrawableRes drawableId: Int): Drawable
}