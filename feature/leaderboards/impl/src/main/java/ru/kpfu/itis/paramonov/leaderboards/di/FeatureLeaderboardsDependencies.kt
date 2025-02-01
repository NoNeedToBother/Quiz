package ru.kpfu.itis.paramonov.leaderboards.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository

interface FeatureLeaderboardsDependencies {

    fun resultRepository(): ResultRepository

    fun settingsRepository(): QuestionSettingsRepository

    fun resourceManager(): ResourceManager

    fun dispatcher(): CoroutineDispatcher
}