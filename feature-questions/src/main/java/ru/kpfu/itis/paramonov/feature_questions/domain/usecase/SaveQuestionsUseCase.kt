package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.local_database_api.external.domain.model.AnswerModel
import ru.kpfu.itis.paramonov.local_database_api.external.domain.model.QuestionModel
import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.SavedQuestionRepository
import javax.inject.Inject

class SaveQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository,
    private val questionSettingsApiModelMapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(questions: List<QuestionDataUiModel>) {
        withContext(dispatcher) {
            val questionsToSave = questions.map {
                val difficulty = it.difficulty?.let { model ->
                    questionSettingsApiModelMapper.mapDifficulty(model)
                } ?: questionSettingsRepository.getDifficulty()
                val category = it.category?.let { model ->
                    questionSettingsApiModelMapper.mapCategory(model)
                } ?: questionSettingsRepository.getCategory()
                QuestionModel(
                    text = it.text, difficulty = difficulty, category = category,
                    answers = it.answers.map {  answer ->
                        AnswerModel(text = answer.answer, isCorrect = answer.correct)
                    }
                )
            }
            savedQuestionRepository.saveQuestions(questionsToSave)
        }

    }
}