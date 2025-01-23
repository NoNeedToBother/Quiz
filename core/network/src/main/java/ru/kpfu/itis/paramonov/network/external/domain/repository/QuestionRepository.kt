package ru.kpfu.itis.paramonov.network.external.domain.repository

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.network.external.domain.model.QuestionDomainModel

interface QuestionRepository {
    suspend fun getCategoryCode(category: Category): Int
    suspend fun getQuestions(
        amount: Int,
        difficulty: Difficulty,
        category: Int
    ): QuestionDomainModel
}