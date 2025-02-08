package ru.kpfu.itis.paramonov.database.external.repository

import ru.kpfu.itis.paramonov.database.external.model.QuestionModel

interface SavedQuestionRepository {

    suspend fun getQuestions(limit: Int): List<QuestionModel>

    suspend fun saveQuestions(questions: List<QuestionModel>)

}
