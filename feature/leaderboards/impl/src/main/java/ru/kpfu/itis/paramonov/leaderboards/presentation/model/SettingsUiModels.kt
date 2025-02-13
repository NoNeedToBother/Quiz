package ru.kpfu.itis.paramonov.leaderboards.presentation.model

enum class DifficultyUiModel {
    EASY, MEDIUM, HARD;
}

enum class CategoryUiModel {
    GENERAL, BOOK, FILM, MUSIC, TV, VIDEO_GAMES, SPORTS, GEOGRAPHY, HISTORY, ANIMALS
}

enum class GameModeUiModel  {
    BLITZ, CHALLENGE, EXPERT
}

data class SettingUiModel(
    val difficulty: DifficultyUiModel?,
    val category: CategoryUiModel?,
    val gameMode: GameModeUiModel
)
