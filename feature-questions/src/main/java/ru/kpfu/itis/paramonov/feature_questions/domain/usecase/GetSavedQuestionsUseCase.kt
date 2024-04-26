package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionData
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository
import javax.inject.Inject

class GetSavedQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper
) {

    suspend operator fun invoke(): List<QuestionData> {
        return withContext(dispatcher) {
            val questions = savedQuestionRepository.getAllQuestions()
            val res = questions.map {
                QuestionData(
                    text = it.text,
                    difficulty = questionSettingsUiModelMapper.mapDifficulty(it.difficulty),
                    category = questionSettingsUiModelMapper.mapCategory(it.category),
                    answers = it.answers.map { answer ->
                        AnswerData(answer.text, false, answer.isCorrect)
                    }
                )
            }
            res
        }
    }
}