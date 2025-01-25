package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val questionRepository: QuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository,
    private val questionUiModelMapper: QuestionUiModelMapper,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper
) {

    suspend operator fun invoke(): List<QuestionDataUiModel> {
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
            val questions = questionRepository.getQuestions(
                amount = amount, difficulty = difficulty, category = categoryCode)
            questionUiModelMapper.map(questions).apply {
                for (question in this) {
                    question.difficulty = questionSettingsUiModelMapper.mapDifficulty(difficulty)
                    question.category = questionSettingsUiModelMapper.mapCategory(category)
                    question.gameMode = questionSettingsUiModelMapper.mapGameMode(gameMode)
                }
            }
        }
    }

    companion object {
        private const val BLITZ_QUESTION_AMOUNT = 10
        private const val CHALLENGE_QUESTION_AMOUNT = 15
        private const val EXPERT_QUESTION_AMOUNT = 25
    }
}