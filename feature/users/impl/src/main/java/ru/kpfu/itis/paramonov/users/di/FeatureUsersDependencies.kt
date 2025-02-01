package ru.kpfu.itis.paramonov.users.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository

interface FeatureUsersDependencies {

    fun userRepository(): UserRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

}