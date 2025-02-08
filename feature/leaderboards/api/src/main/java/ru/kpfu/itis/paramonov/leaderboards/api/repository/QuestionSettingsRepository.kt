package ru.kpfu.itis.paramonov.leaderboards.api.repository

import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

interface QuestionSettingsRepository {

    fun getDifficulty(): Difficulty

    fun getGameMode(): GameMode

}
