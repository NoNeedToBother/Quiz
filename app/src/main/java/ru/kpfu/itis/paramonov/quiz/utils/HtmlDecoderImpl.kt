package ru.kpfu.itis.paramonov.quiz.utils

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder

class HtmlDecoderImpl: HtmlDecoder {

    @SuppressLint("ObsoleteSdkInt") //just in case
    override fun decode(encoded: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(encoded, Html.FROM_HTML_MODE_COMPACT).toString()
        } else {
            Html.fromHtml(encoded).toString()
        }
    }
}