package ru.kpfu.itis.paramonov.network.external.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder

interface QuestionsDependencies {

    fun resourceManager(): ResourceManager

    fun htmlDecoder(): HtmlDecoder

    fun dispatcher(): CoroutineDispatcher

}