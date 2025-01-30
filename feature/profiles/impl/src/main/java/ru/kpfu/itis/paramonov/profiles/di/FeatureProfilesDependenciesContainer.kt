package ru.kpfu.itis.paramonov.profiles.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository

interface FeatureProfilesDependenciesContainer: FeatureContainer {

    fun friendRepository(): FriendRepository

    fun resultRepository(): ResultRepository

    fun userRepository(): UserRepository
}