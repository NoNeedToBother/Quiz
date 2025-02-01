package ru.kpfu.itis.paramonov.leaderboards.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.leaderboards.api.api.FeatureLeaderboardsApi

interface FeatureLeaderboardsDependenciesContainer: FeatureContainer {

    fun featureLeaderboardsApi(): FeatureLeaderboardsApi

}