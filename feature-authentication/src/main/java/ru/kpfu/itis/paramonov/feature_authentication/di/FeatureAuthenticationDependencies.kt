package ru.kpfu.itis.paramonov.feature_authentication.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository

interface FeatureAuthenticationDependencies {

    fun authenticationRepository(): AuthenticationRepository

    fun dispatcher(): CoroutineDispatcher

    fun usernameValidator(): UsernameValidator

    fun passwordValidator(): PasswordValidator
}