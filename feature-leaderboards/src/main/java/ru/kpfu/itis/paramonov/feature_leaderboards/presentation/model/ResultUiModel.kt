package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class ResultUiModel(
    val id: String,
    val user: UserModel,
    val time: Int,
    val correct: Int,
    val total: Int,
    val score: Double,
    val difficulty: DifficultyUiModel,
    val category: CategoryUiModel,
    val gameMode: GameModeUiModel,
)