package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi

interface FirebaseContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}