package ru.kpfu.itis.paramonov.firebase.external.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser

interface FirebaseDependencies {
    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun dateTimeParser(): DateTimeParser
}