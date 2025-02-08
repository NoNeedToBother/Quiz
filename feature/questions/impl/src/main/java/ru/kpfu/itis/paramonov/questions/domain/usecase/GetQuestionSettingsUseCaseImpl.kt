package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.model.QuestionSettings
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase

class GetQuestionSettingsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
): GetQuestionSettingsUseCase {

    override suspend operator fun invoke(): QuestionSettings {
        return withContext(dispatcher) {
            val difficulty = repository.getDifficulty()
            val category = repository.getCategory()
            val gameMode = repository.getGameMode()
            QuestionSettings(
                difficulty, category, gameMode
            )
        }
    }
}
