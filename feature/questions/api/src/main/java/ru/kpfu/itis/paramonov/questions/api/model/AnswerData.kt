package ru.kpfu.itis.paramonov.questions.api.model

data class AnswerData(
    val answer: String,
    var chosen: Boolean,
    val correct: Boolean
)