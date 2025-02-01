package ru.kpfu.itis.paramonov.authentication.di

import ru.kpfu.itis.paramonov.authentication.api.api.FeatureAuthenticationApi
import ru.kpfu.itis.paramonov.core.di.FeatureContainer

interface FeatureAuthenticationDependenciesContainer: FeatureContainer {

    fun featureAuthenticationApi(): FeatureAuthenticationApi
}