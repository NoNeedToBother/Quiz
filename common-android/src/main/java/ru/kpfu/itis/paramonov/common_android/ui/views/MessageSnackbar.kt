package ru.kpfu.itis.paramonov.common_android.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.paramonov.common_android.R

class MessageSnackbar(
    context: Context,
    anchor: View,
    message: String
    ) {

    private val snackbar: Snackbar

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.snackbar_message, null)
        view.findViewById<TextView>(R.id.tv_message).text = message
        snackbar = Snackbar
            .make(anchor, message, Snackbar.LENGTH_LONG)
        with (snackbar.view as Snackbar.SnackbarLayout) {
            setBackgroundColor(Color.TRANSPARENT)
            addView(view, 0)
        }
    }

    @SuppressLint("RestrictedApi")
    fun adjustPosition(marginDefault: Int,
                       marginLeft: Int = marginDefault,
                       marginTop: Int = marginDefault,
                       marginRight: Int = marginDefault,
                       marginBottom: Int = marginDefault) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        with (snackbar.view as Snackbar.SnackbarLayout) {
            setLayoutParams(layoutParams)
        }
    }

    fun show() {
        snackbar.show()
    }
}
