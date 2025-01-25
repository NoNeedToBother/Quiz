package ru.kpfu.itis.paramonov.database.external.domain.model

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty

data class QuestionModel(
    val text: String,
    val difficulty: Difficulty,
    val category: Category,
    val answers: List<AnswerModel>
)