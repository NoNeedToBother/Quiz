package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel
import ru.kpfu.itis.paramonov.local_database_api.domain.model.GameMode
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val questionRepository: QuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository,
    private val mapper: QuestionUiModelMapper
) {

    suspend operator fun invoke(): QuestionUiModel {
        return withContext(dispatcher) {
            val difficulty = questionSettingsRepository.getDifficulty()
            val category = questionSettingsRepository.getCategory()
            val gameMode = questionSettingsRepository.getGameMode()
            val amount = when (gameMode) {
                GameMode.BLITZ -> BLITZ_QUESTION_AMOUNT
            }
            val categoryCode = questionRepository.getCategoryCode(category.name)
            val questions = questionRepository.getQuestions(
                amount,
                difficulty.name.normalizeEnumName().lowercase(),
                categoryCode
            )
            mapper.map(questions)
        }
    }

    companion object {
        private const val BLITZ_QUESTION_AMOUNT = 15
    }
}