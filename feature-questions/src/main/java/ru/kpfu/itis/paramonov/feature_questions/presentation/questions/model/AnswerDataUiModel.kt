package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

data class AnswerDataUiModel(
    val answer: String,
    var chosen: Boolean,
    val correct: Boolean
)