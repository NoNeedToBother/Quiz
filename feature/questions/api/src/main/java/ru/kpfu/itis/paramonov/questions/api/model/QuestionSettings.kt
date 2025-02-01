package ru.kpfu.itis.paramonov.questions.api.model

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

data class QuestionSettings(
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode
)