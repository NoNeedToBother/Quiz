package ru.kpfu.itis.paramonov.firebase.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

interface FirebaseDependencies {
    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager
}