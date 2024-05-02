package ru.kpfu.itis.paramonov.feature_authentication.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository

interface FeatureAuthenticationDependencies {

    fun authenticationRepository(): AuthenticationRepository

    fun dispatcher(): CoroutineDispatcher
}