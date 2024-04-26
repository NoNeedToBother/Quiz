package ru.kpfu.itis.paramonov.quiz.di.local_database

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher

interface LocalDatabaseDependencies {

    fun context(): Context

    fun dispatcher(): CoroutineDispatcher
}