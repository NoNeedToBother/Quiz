package ru.kpfu.itis.paramonov.leaderboards.domain.mapper

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.GameModeUiModel

class QuestionSettingsUiModelMapper {
    fun mapDifficulty(difficulty: Difficulty): DifficultyUiModel {
        return when(difficulty) {
            Difficulty.EASY -> DifficultyUiModel.EASY
            Difficulty.MEDIUM -> DifficultyUiModel.MEDIUM
            Difficulty.HARD -> DifficultyUiModel.HARD
        }
    }

    fun mapCategory(category: Category): CategoryUiModel {
        return when(category) {
            Category.ANIMALS -> CategoryUiModel.ANIMALS
            Category.GENERAL -> CategoryUiModel.GENERAL
            Category.BOOK -> CategoryUiModel.BOOK
            Category.FILM -> CategoryUiModel.FILM
            Category.MUSIC -> CategoryUiModel.MUSIC
            Category.TV -> CategoryUiModel.TV
            Category.VIDEO_GAMES -> CategoryUiModel.VIDEO_GAMES
            Category.SPORTS -> CategoryUiModel.SPORTS
            Category.GEOGRAPHY -> CategoryUiModel.GEOGRAPHY
            Category.HISTORY -> CategoryUiModel.HISTORY
        }
    }

    fun mapGameMode(gameMode: GameMode): GameModeUiModel {
        return when(gameMode) {
            GameMode.BLITZ -> GameModeUiModel.BLITZ
            GameMode.CHALLENGE -> GameModeUiModel.CHALLENGE
            GameMode.EXPERT -> GameModeUiModel.EXPERT
        }
    }
}
