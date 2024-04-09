package ru.kpfu.itis.paramonov.common.resources

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

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

}