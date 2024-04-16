package ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings

data class QuestionSettingsUiModel(
    val difficulty: DifficultyUiModel,
    val category: CategoryUiModel,
    val gameMode: GameModeUiModel
)