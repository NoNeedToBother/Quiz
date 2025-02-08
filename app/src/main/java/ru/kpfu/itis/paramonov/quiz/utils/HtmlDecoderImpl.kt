package ru.kpfu.itis.paramonov.quiz.utils

import android.text.Html
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder

class HtmlDecoderImpl: HtmlDecoder {

    override fun decode(encoded: String): String {
        return Html.fromHtml(encoded, Html.FROM_HTML_MODE_COMPACT).toString()
    }
}
