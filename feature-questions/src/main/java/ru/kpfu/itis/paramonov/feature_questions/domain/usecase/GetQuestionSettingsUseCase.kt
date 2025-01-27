package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.QuestionSettingsUiModel
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import javax.inject.Inject

class GetQuestionSettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository,
    private val mapper: QuestionSettingsUiModelMapper
) {

    suspend operator fun invoke(): QuestionSettingsUiModel {
        return withContext(dispatcher) {
            val difficulty = repository.getDifficulty()
            val category = repository.getCategory()
            val gameMode = repository.getGameMode()
            QuestionSettingsUiModel(
                difficulty = mapper.mapDifficulty(difficulty),
                category = mapper.mapCategory(category),
                gameMode = mapper.mapGameMode(gameMode)
            )
        }
    }
}