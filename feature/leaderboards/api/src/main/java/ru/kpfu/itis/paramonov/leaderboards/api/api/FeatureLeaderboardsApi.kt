package ru.kpfu.itis.paramonov.leaderboards.api.api

import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository

interface FeatureLeaderboardsApi {

    fun resultRepository(): ResultRepository

    fun questionSettingsRepository(): QuestionSettingsRepository

}