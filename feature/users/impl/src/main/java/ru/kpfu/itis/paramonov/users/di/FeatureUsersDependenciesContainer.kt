package ru.kpfu.itis.paramonov.users.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.users.api.api.FeatureUsersApi

interface FeatureUsersDependenciesContainer: FeatureContainer {

    fun featureUsersApi(): FeatureUsersApi

}