package ru.kpfu.itis.paramonov.leaderboards.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository

interface FeatureLeaderboardsDependenciesContainer: FeatureContainer {

    fun resultRepository(): ResultRepository

    fun questionSettingsRepository(): QuestionSettingsRepository

}