package ru.kpfu.itis.paramonov.common_android.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder

interface CommonApi {

    fun context(): Context

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher

    fun htmlDecoder(): HtmlDecoder

    fun dateTimeParser(): DateTimeParser
}