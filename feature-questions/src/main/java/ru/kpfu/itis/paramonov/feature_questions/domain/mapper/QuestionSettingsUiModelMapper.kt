package ru.kpfu.itis.paramonov.feature_questions.domain.mapper

import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import javax.inject.Inject

class QuestionSettingsUiModelMapper @Inject constructor() {
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
        }
    }
}