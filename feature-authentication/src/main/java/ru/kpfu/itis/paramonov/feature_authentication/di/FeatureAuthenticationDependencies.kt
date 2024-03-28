package ru.kpfu.itis.paramonov.feature_authentication.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FeatureAuthenticationDependencies {

    fun repository(): UserRepository

    fun dispatcher(): CoroutineDispatcher
}