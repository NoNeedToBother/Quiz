package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

interface FeatureAuthenticationDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}