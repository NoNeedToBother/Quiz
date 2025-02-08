package ru.kpfu.itis.paramonov.database.internal.room.repository

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.database.internal.room.database.QuestionDatabase
import ru.kpfu.itis.paramonov.database.internal.room.entity.AnswerEntity
import ru.kpfu.itis.paramonov.database.internal.room.entity.QuestionEntity
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.database.external.model.AnswerModel
import ru.kpfu.itis.paramonov.database.external.model.QuestionModel
import ru.kpfu.itis.paramonov.database.external.repository.SavedQuestionRepository

internal class SavedQuestionRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val questionDatabase: QuestionDatabase
): SavedQuestionRepository {
    override suspend fun getQuestions(limit: Int): List<QuestionModel> {
        return withContext(dispatcher) {
            val questions = questionDatabase.questionDao().getQuestions(limit)
            questions.map {
                QuestionModel(
                    text = it.question.text,
                    difficulty = Difficulty.valueOf(it.question.difficulty),
                    category = Category.valueOf(it.question.category),
                    answers = it.answers.map { answer ->
                        AnswerModel(text = answer.text, isCorrect = answer.correct)
                    }
                )
            }
        }
    }

    override suspend fun saveQuestions(questions: List<QuestionModel>) {
        for (question in questions) {
            saveQuestion(question)
        }
    }

    private suspend fun saveQuestion(questionModel: QuestionModel) {
        val question = QuestionEntity(
            text = questionModel.text,
            difficulty = questionModel.difficulty.name,
            category = questionModel.category.name
        )

        withContext(dispatcher) {
            try {
                val questionId = questionDatabase.questionDao().save(question)

                for (answer in questionModel.answers) {
                    val answerEntity = AnswerEntity(
                        text = answer.text, correct = answer.isCorrect, questionId = questionId
                    )
                    questionDatabase.answerDao().save(answerEntity)
                }
            } catch (_: SQLiteException) { }
        }
    }
}
