package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel

data class QuestionDataUiModel(
    val text: String,
    val answer: String,
    val incorrectAnswers: List<String>
) {
    var difficulty: DifficultyUiModel? = null
    var category: CategoryUiModel? = null
}