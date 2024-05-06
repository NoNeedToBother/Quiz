package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

interface FeatureUsersDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}