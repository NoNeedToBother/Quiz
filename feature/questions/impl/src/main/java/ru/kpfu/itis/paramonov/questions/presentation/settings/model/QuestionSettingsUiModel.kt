package ru.kpfu.itis.paramonov.questions.presentation.settings.model

data class QuestionSettingsUiModel(
    val difficulty: DifficultyUiModel,
    val category: CategoryUiModel,
    val gameMode: GameModeUiModel
)