package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionsUseCase

class GetQuestionsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val questionRepository: QuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository
): GetQuestionsUseCase {

    override suspend operator fun invoke(): List<QuestionData> {
        return withContext(dispatcher) {
            val difficulty = questionSettingsRepository.getDifficulty()
            val category = questionSettingsRepository.getCategory()
            val gameMode = questionSettingsRepository.getGameMode()
            val amount = when (gameMode) {
                GameMode.BLITZ -> BLITZ_QUESTION_AMOUNT
                GameMode.CHALLENGE -> CHALLENGE_QUESTION_AMOUNT
                GameMode.EXPERT -> EXPERT_QUESTION_AMOUNT
            }
            val categoryCode = questionRepository.getCategoryCode(category)
            questionRepository.getQuestions(
                amount = amount, difficulty = difficulty, category = categoryCode).questions
        }
    }

    companion object {
        private const val BLITZ_QUESTION_AMOUNT = 10
        private const val CHALLENGE_QUESTION_AMOUNT = 15
        private const val EXPERT_QUESTION_AMOUNT = 25
    }
}
