package ru.kpfu.itis.paramonov.feature_questions.domain.mapper

import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import javax.inject.Inject

class QuestionSettingsApiModelMapper @Inject constructor() {
    fun mapDifficulty(difficulty: DifficultyUiModel): Difficulty {
        return when(difficulty) {
            DifficultyUiModel.EASY -> Difficulty.EASY
            DifficultyUiModel.MEDIUM -> Difficulty.MEDIUM
            DifficultyUiModel.HARD -> Difficulty.HARD
        }
    }

    fun mapCategory(category: CategoryUiModel): Category {
        return when(category) {
            CategoryUiModel.ANIMALS -> Category.ANIMALS
            CategoryUiModel.GENERAL -> Category.GENERAL
            CategoryUiModel.BOOK -> Category.BOOK
            CategoryUiModel.FILM -> Category.FILM
            CategoryUiModel.MUSIC -> Category.MUSIC
            CategoryUiModel.TV -> Category.TV
            CategoryUiModel.VIDEO_GAMES -> Category.VIDEO_GAMES
            CategoryUiModel.SPORTS -> Category.SPORTS
            CategoryUiModel.GEOGRAPHY -> Category.GEOGRAPHY
            CategoryUiModel.HISTORY -> Category.HISTORY
        }
    }

    fun mapGameMode(gameMode: GameModeUiModel): GameMode {
        return when(gameMode) {
            GameModeUiModel.BLITZ -> GameMode.BLITZ
            GameModeUiModel.CHALLENGE -> GameMode.CHALLENGE
            GameModeUiModel.EXPERT -> GameMode.EXPERT
        }
    }
}