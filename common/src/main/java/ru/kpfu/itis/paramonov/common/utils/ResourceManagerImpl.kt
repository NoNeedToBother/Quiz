package ru.kpfu.itis.paramonov.common.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ResourceManager {
    override fun getString(@StringRes stringId: Int): String = context.resources.getString(stringId)

    override fun getString(@StringRes stringId: Int, vararg args: Any?): String {
        return context.resources.getString(stringId, *args)
    }
}