package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase

class SaveQuestionSettingsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: QuestionSettingsRepository
): SaveQuestionSettingsUseCase {

    override suspend operator fun invoke(
        difficulty: Difficulty?,
        category: Category?,
        gameMode: GameMode?) {
        withContext(dispatcher) {
            difficulty?.let {
                repository.saveDifficulty(it)
            }
            category?.let {
                repository.saveCategory(it)
            }
            gameMode?.let {
                repository.saveGameMode(it)
            }
        }
    }
}
