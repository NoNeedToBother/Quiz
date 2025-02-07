package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.model.AnswerData
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.GetSavedQuestionsUseCase

class GetSavedQuestionsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionsSettingsRepository: QuestionSettingsRepository
): GetSavedQuestionsUseCase {

    override suspend operator fun invoke(): List<QuestionData> {
        return withContext(dispatcher) {
            val limit = questionsSettingsRepository.getLimit()
            val questions = savedQuestionRepository.getQuestions(limit)
            questions.map {
                QuestionData(
                    text = it.text,
                    answers = it.answers.map { answer ->
                        AnswerData(answer.text, false, answer.isCorrect)
                    }
                ).apply {
                    difficulty = it.difficulty
                    category = it.category
                }
            }
        }
    }
}