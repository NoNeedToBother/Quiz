package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

data class AnswerData(
    val answer: String,
    var chosen: Boolean,
    val correct: Boolean
)