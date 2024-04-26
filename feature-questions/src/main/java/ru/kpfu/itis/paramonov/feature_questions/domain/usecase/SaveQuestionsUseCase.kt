package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsDomainModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionData
import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.AnswerModel
import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.QuestionModel
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository
import javax.inject.Inject

class SaveQuestionsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository,
    private val questionSettingsDomainModelMapper: QuestionSettingsDomainModelMapper
) {

    suspend operator fun invoke(questions: List<QuestionData>) {
        withContext(dispatcher) {
            val questionsToSave = questions.map {
                val difficulty = it.difficulty?.let { model ->
                    questionSettingsDomainModelMapper.mapDifficulty(model)
                } ?: questionSettingsRepository.getDifficulty()
                val category = it.category?.let { model ->
                    questionSettingsDomainModelMapper.mapCategory(model)
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