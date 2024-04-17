package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model

data class QuestionSettingsUiModel(
    val difficulty: DifficultyUiModel,
    val category: CategoryUiModel,
    val gameMode: GameModeUiModel
)