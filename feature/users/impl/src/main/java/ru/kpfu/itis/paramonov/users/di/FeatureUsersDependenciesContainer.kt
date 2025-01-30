package ru.kpfu.itis.paramonov.users.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository

interface FeatureUsersDependenciesContainer: FeatureContainer {

    fun userRepository(): UserRepository

}