package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi

interface FeatureUsersDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}