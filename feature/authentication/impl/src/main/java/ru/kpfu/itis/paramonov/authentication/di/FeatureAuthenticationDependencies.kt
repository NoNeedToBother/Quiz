package ru.kpfu.itis.paramonov.authentication.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

interface FeatureAuthenticationDependencies {

    fun authenticationRepository(): AuthenticationRepository

    fun dispatcher(): CoroutineDispatcher

    fun usernameValidator(): UsernameValidator

    fun passwordValidator(): PasswordValidator
}