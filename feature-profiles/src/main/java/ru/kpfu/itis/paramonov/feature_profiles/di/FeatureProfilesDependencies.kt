package ru.kpfu.itis.paramonov.feature_profiles.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.validators.PasswordValidator
import ru.kpfu.itis.paramonov.common.validators.UsernameValidator
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository

interface FeatureProfilesDependencies {

    fun userRepository(): UserRepository

    fun friendRepository(): FriendRepository

    fun resultRepository(): ResultRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun passwordValidator(): PasswordValidator

    fun usernameValidator(): UsernameValidator

}