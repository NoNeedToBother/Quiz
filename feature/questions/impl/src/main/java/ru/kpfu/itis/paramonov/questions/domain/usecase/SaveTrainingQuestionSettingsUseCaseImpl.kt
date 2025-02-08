package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveTrainingQuestionSettingsUseCase

class SaveTrainingQuestionSettingsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
): SaveTrainingQuestionSettingsUseCase {

    override suspend operator fun invoke(limit: Int) {
        withContext(dispatcher) {
            repository.saveLimit(limit)
        }
    }
}
