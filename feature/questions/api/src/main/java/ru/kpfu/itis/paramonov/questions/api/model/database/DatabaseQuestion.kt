package ru.kpfu.itis.paramonov.questions.api.model.database

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty

data class DatabaseQuestion(
    val text: String,
    val difficulty: Difficulty,
    val category: Category,
    val answers: List<DatabaseAnswer>
)
