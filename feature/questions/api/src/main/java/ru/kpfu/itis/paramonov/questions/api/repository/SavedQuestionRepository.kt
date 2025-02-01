package ru.kpfu.itis.paramonov.questions.api.repository

import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion

interface SavedQuestionRepository {

    suspend fun getQuestions(limit: Int): List<DatabaseQuestion>

    suspend fun saveQuestions(questions: List<DatabaseQuestion>)

}