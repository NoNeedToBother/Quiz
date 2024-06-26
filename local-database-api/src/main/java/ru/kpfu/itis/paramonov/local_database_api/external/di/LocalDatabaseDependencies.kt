package ru.kpfu.itis.paramonov.local_database_api.external.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

interface LocalDatabaseDependencies {

    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager
}