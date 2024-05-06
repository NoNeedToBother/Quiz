package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.TrainingQuestionSettingsUiModel
import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.QuestionSettingsRepository
import javax.inject.Inject

class GetTrainingQuestionSettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
) {

    suspend operator fun invoke(): TrainingQuestionSettingsUiModel {
        return withContext(dispatcher) {
            val limit = repository.getLimit()
            TrainingQuestionSettingsUiModel(
                limit = limit
            )
        }
    }
}