package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

interface FeatureAuthenticationDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}