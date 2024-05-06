package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

abstract class FeatureAuthenticationApiHolder(
    private val mFeatureContainer: FeatureAuthenticationDependenciesContainer
): FeatureApiHolder(mFeatureContainer) {

    fun firebaseApi(): FirebaseApi {
        return mFeatureContainer.firebaseApi()
    }
}