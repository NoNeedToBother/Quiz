package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseContainer

abstract class FirebaseFeatureApiHolder(
    private val mFeatureContainer: FirebaseContainer
): FeatureApiHolder(mFeatureContainer) {

    fun firebaseApi(): FirebaseApi {
        return mFeatureContainer.firebaseApi()
    }
}