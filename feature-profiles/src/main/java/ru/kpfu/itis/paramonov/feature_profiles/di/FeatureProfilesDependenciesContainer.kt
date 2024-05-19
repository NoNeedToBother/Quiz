package ru.kpfu.itis.paramonov.feature_profiles.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

interface FeatureProfilesDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}