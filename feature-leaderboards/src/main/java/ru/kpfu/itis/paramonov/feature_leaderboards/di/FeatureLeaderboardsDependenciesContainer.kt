package ru.kpfu.itis.paramonov.feature_leaderboards.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi

interface FeatureLeaderboardsDependenciesContainer: FeatureContainer {

    fun firebaseApi(): FirebaseApi

}