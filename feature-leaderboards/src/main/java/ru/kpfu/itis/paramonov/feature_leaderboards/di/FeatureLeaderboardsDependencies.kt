package ru.kpfu.itis.paramonov.feature_leaderboards.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository

interface FeatureLeaderboardsDependencies {

    fun resultRepository(): ResultRepository

    fun settingsRepository(): QuestionSettingsRepository

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher
}