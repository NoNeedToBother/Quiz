package ru.kpfu.itis.paramonov.feature_leaderboards.data.datasource

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode

data class ResultSettingsMediator(
    val gameMode: GameMode,
    val difficulty: Difficulty?,
    val category: Category?,
    val after: Double?
)