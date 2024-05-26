package ru.kpfu.itis.paramonov.common_android.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

fun Context.spToPx(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics)
}


fun <T> Flow<T>.collect(lifecycleOwner: LifecycleOwner, action: suspend (T) -> Unit) {
    onEach {
        action.invoke(it)
    }.flowWithLifecycle(lifecycleOwner.lifecycle).launchIn(lifecycleOwner.lifecycleScope)
}

fun <T> MutableStateFlow<T?>.emitException(value: T) {
    this.value = value
    this.value = null
}

fun Fragment.startPostponedTransition() {
    (view?.parent as? ViewGroup?)?.doOnPreDraw {
        startPostponedEnterTransition()
    }
}

fun View.setAndUpdate(action: () -> Unit) {
    action.invoke()
    invalidate()
}