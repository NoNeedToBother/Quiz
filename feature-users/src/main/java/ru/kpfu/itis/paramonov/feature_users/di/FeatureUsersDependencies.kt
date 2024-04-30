package ru.kpfu.itis.paramonov.feature_users.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FeatureUsersDependencies {

    fun userRepository(): UserRepository

    fun dispatcher(): CoroutineDispatcher

}