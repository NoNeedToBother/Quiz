package ru.kpfu.itis.paramonov.feature_users.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.domain.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FeatureUsersDependencies {

    fun userRepository(): UserRepository

    fun friendRepository(): FriendRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

}