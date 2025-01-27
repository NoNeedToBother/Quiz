package ru.kpfu.itis.paramonov.database.external.domain.repository

import ru.kpfu.itis.paramonov.database.external.domain.model.QuestionModel

interface SavedQuestionRepository {

    suspend fun getQuestions(limit: Int): List<QuestionModel>

    suspend fun saveQuestions(questions: List<QuestionModel>)

}