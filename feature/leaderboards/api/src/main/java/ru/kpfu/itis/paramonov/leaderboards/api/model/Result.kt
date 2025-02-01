package ru.kpfu.itis.paramonov.leaderboards.api.model

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.core.utils.DateTime

class Result(
    val id: String,
    val user: User,
    val time: Int,
    val correct: Int,
    val total: Int,
    val score: Double,
    val date: DateTime,
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode
)