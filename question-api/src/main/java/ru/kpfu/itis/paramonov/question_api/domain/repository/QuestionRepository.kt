package ru.kpfu.itis.paramonov.question_api.domain.repository

import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel

interface QuestionRepository {
    suspend fun getQuestions(amount: Int, difficulty: String, category: Int): QuestionDomainModel
}