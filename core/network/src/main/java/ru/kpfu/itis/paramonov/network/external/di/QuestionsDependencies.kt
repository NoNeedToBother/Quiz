package ru.kpfu.itis.paramonov.network.external.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder

interface QuestionsDependencies {

    fun resourceManager(): ResourceManager

    fun htmlDecoder(): HtmlDecoder

    fun dispatcher(): CoroutineDispatcher

}