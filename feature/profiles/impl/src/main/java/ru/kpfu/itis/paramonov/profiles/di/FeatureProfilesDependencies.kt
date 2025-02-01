package ru.kpfu.itis.paramonov.profiles.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository

interface FeatureProfilesDependencies {

    fun userRepository(): UserRepository

    fun friendRepository(): FriendRepository

    fun resultRepository(): ResultRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun passwordValidator(): PasswordValidator

    fun usernameValidator(): UsernameValidator

}