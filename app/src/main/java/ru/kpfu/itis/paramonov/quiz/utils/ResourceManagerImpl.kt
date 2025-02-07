package ru.kpfu.itis.paramonov.quiz.utils

import android.content.Context
import androidx.annotation.StringRes
import ru.kpfu.itis.paramonov.core.resources.ResourceManager

class ResourceManagerImpl (
    private val context: Context
): ResourceManager {
    override fun getString(@StringRes stringId: Int): String = context.resources.getString(stringId)

    override fun getString(@StringRes stringId: Int, vararg args: Any?): String {
        return context.resources.getString(stringId, *args)
    }
}