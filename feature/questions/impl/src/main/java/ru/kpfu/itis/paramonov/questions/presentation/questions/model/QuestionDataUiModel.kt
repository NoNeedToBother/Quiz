package ru.kpfu.itis.paramonov.questions.presentation.questions.model

import ru.kpfu.itis.paramonov.questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.GameModeUiModel

class QuestionDataUiModel(
    val text: String,
    val answers: List<AnswerDataUiModel>,
) {

    var difficulty: DifficultyUiModel? = null

    var category: CategoryUiModel? = null

    var gameMode: GameModeUiModel? = null

}
