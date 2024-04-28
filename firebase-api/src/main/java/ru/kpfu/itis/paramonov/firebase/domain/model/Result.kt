package ru.kpfu.itis.paramonov.firebase.domain.model

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode

class Result(
    val userId: String,
    val time: Int,
    val correct: Int,
    val total: Int,
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode,
) {

    private var _score: Double? = null

    var score: Double = -1.0
        get() = _score as Double
        private set


    internal constructor(userId: String, time: Int, correct: Int, total: Int, score: Double,
                         difficulty: Difficulty, category: Category, gameMode: GameMode):
            this(userId, time, correct, total, difficulty, category, gameMode) {
                _score = score
            }
}