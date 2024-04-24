package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi

abstract class FirebaseFeatureApiHolder(
    private val mFeatureContainer: FeatureAuthenticationDependenciesContainer
): FeatureApiHolder(mFeatureContainer) {

    fun firebaseApi(): FirebaseApi {
        return mFeatureContainer.firebaseApi()
    }
}