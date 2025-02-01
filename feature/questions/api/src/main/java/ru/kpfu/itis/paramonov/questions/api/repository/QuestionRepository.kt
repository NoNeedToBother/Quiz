package ru.kpfu.itis.paramonov.questions.api.repository

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.questions.api.model.Question

interface QuestionRepository {

    suspend fun getCategoryCode(category: Category): Int
    suspend fun getQuestions(
        amount: Int,
        difficulty: Difficulty,
        category: Int
    ): Question

}