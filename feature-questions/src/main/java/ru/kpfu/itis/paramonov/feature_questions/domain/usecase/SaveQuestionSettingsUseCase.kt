package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import javax.inject.Inject

class SaveQuestionSettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository,
    private val mapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(
        difficulty: DifficultyUiModel?,
        category: CategoryUiModel?,
        gameMode: GameModeUiModel?) {
        withContext(dispatcher) {
            difficulty?.let {
                repository.saveDifficulty(mapper.mapDifficulty(it))
            }
            category?.let {
                repository.saveCategory(mapper.mapCategory(it))
            }
            gameMode?.let {
                repository.saveGameMode(mapper.mapGameMode(it))
            }
        }
    }
}