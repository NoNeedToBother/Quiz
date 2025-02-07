package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseAnswer
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionsUseCase

class SaveQuestionsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val savedQuestionRepository: SavedQuestionRepository,
    private val questionSettingsRepository: QuestionSettingsRepository
): SaveQuestionsUseCase {

    override suspend operator fun invoke(questions: List<QuestionData>) {
        withContext(dispatcher) {
            val questionsToSave = questions.map {
                val difficulty = it.difficulty ?: questionSettingsRepository.getDifficulty()
                val category = it.category ?: questionSettingsRepository.getCategory()
                DatabaseQuestion(
                    text = it.text, difficulty = difficulty, category = category,
                    answers = it.answers.map {  answer ->
                        DatabaseAnswer(text = answer.answer, isCorrect = answer.correct)
                    }
                )
            }
            savedQuestionRepository.saveQuestions(questionsToSave)
        }
    }
}