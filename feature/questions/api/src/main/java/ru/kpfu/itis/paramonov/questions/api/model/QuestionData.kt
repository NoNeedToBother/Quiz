package ru.kpfu.itis.paramonov.questions.api.model

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

class QuestionData(
    val text: String,
    val answers: List<AnswerData>,
) {

    var difficulty: Difficulty? = null

    var category: Category? = null

    var gameMode: GameMode? = null

}
