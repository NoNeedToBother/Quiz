package ru.kpfu.itis.paramonov.database.external.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager

interface LocalDatabaseDependencies {

    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager
}