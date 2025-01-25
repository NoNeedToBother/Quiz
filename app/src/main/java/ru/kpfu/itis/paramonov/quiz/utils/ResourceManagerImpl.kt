package ru.kpfu.itis.paramonov.quiz.utils

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ru.kpfu.itis.paramonov.core.resources.ResourceManager

class ResourceManagerImpl (
    private val context: Context
): ResourceManager {
    override fun getString(@StringRes stringId: Int): String = context.resources.getString(stringId)

    override fun getString(@StringRes stringId: Int, vararg args: Any?): String {
        return context.resources.getString(stringId, *args)
    }

    override fun getDrawable(drawableId: Int): Drawable {
        return ContextCompat.getDrawable(context, drawableId)
            ?: throw NotFoundException()
    }

    override fun getTheme(themeId: Int): Resources.Theme {
        return context.theme
    }

    override fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    override fun pxToDp(px: Float): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    override fun loadAnimation(animationId: Int): Animation {
        return AnimationUtils.loadAnimation(context, animationId)
    }

}