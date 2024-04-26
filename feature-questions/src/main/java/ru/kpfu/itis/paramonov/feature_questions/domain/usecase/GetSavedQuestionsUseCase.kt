package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository
import javax.inject.Inject

class GetSavedQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper
) {

    suspend operator fun invoke(): List<QuestionDataUiModel> {
        return withContext(dispatcher) {
            val questions = savedQuestionRepository.getAllQuestions()
            questions.map {
                QuestionDataUiModel(
                    text = it.text,
                    answers = it.answers.map { answer ->
                        AnswerDataUiModel(answer.text, false, answer.isCorrect)
                    }
                ).apply {
                    difficulty = questionSettingsUiModelMapper.mapDifficulty(it.difficulty)
                    category = questionSettingsUiModelMapper.mapCategory(it.category)
                }
            }
        }
    }
}