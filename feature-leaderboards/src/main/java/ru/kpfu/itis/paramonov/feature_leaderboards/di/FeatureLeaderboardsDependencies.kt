package ru.kpfu.itis.paramonov.feature_leaderboards.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository

interface FeatureLeaderboardsDependencies {

    fun resultRepository(): ResultRepository

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher
}