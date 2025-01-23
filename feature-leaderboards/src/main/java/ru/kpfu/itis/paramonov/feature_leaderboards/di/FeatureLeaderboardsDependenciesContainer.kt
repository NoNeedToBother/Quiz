package ru.kpfu.itis.paramonov.feature_leaderboards.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.database.external.domain.api.LocalDatabaseApi

interface FeatureLeaderboardsDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi

    fun localDatabaseApi(): LocalDatabaseApi

}