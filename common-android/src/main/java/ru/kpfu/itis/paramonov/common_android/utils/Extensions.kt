package ru.kpfu.itis.paramonov.common_android.utils

import android.content.Context
import android.util.TypedValue
import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Float.fromDp(ctx: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        ctx.resources.displayMetrics
    )
}