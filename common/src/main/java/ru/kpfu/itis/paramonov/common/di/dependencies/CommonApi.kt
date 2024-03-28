package ru.kpfu.itis.paramonov.common.di.dependencies

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

interface CommonApi {

    fun context(): Context

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher
}