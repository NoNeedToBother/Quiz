package ru.kpfu.itis.paramonov.common.api

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder

interface CommonApi {

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher

    fun htmlDecoder(): HtmlDecoder

    fun dateTimeParser(): DateTimeParser
}