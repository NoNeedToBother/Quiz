package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import javax.inject.Inject

class SaveTrainingQuestionSettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
) {

    suspend operator fun invoke(limit: Int) {
        withContext(dispatcher) {
            repository.saveLimit(limit)
        }
    }
}