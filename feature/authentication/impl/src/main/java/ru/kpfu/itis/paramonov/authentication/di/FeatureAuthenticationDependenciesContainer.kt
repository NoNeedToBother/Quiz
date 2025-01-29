package ru.kpfu.itis.paramonov.authentication.di

import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.core.di.FeatureContainer

interface FeatureAuthenticationDependenciesContainer: FeatureContainer {

    fun authenticationRepository(): AuthenticationRepository
}