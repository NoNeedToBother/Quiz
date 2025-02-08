package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.model.TrainingQuestionSettings
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.GetTrainingQuestionSettingsUseCase

class GetTrainingQuestionSettingsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
): GetTrainingQuestionSettingsUseCase {

    override suspend operator fun invoke(): TrainingQuestionSettings {
        return withContext(dispatcher) {
            val limit = repository.getLimit()
            TrainingQuestionSettings(
                limit = limit
            )
        }
    }
}
