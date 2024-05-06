package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

abstract class FeatureUsersApiHolder(
    private val mFeatureContainer: FeatureUsersDependenciesContainer
): FeatureApiHolder(mFeatureContainer) {

    fun firebaseApi(): FirebaseApi {
        return mFeatureContainer.firebaseApi()
    }
}