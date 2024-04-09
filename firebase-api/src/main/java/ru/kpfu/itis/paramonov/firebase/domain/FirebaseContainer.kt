package ru.kpfu.itis.paramonov.firebase.domain

import ru.kpfu.itis.paramonov.common.di.FeatureContainer

interface FirebaseContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi
}