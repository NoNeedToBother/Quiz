package ru.kpfu.itis.paramonov.questions.api.model

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

class Result(
    val user: User,
    val time: Int,
    val correct: Int,
    val total: Int,
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode
)
