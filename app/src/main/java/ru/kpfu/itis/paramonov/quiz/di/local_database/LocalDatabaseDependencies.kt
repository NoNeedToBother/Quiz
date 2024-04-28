package ru.kpfu.itis.paramonov.quiz.di.local_database

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

interface LocalDatabaseDependencies {

    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager
}