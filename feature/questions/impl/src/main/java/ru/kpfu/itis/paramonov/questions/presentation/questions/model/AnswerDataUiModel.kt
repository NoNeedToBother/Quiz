package ru.kpfu.itis.paramonov.questions.presentation.questions.model

data class AnswerDataUiModel(
    val answer: String,
    var chosen: Boolean,
    val correct: Boolean
)
