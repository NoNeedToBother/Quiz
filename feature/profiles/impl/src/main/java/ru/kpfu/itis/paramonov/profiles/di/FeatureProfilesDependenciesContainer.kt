package ru.kpfu.itis.paramonov.profiles.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.profiles.api.api.FeatureProfilesApi

interface FeatureProfilesDependenciesContainer: FeatureContainer {

    fun featureProfilesApi(): FeatureProfilesApi
}