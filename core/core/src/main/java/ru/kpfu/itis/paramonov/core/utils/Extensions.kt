package ru.kpfu.itis.paramonov.core.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun <T> Flow<T>.collect(lifecycleOwner: LifecycleOwner, action: suspend (T) -> Unit) {
    onEach {
        action.invoke(it)
    }.flowWithLifecycle(lifecycleOwner.lifecycle).launchIn(lifecycleOwner.lifecycleScope)
}

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