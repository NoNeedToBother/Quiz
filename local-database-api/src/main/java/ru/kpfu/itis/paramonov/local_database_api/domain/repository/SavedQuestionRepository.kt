package ru.kpfu.itis.paramonov.local_database_api.domain.repository

import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.QuestionModel

interface SavedQuestionRepository {

    suspend fun getQuestions(limit: Int): List<QuestionModel>

    suspend fun saveQuestions(questions: List<QuestionModel>)

}