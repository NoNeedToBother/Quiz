package ru.kpfu.itis.paramonov.local_database_api.data.room.repository

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.local_database_api.data.room.database.QuestionDatabase
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.AnswerEntity
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.QuestionEntity
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Category
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Difficulty
import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.AnswerModel
import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.QuestionModel
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository

class SavedQuestionRepositoryImpl(
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