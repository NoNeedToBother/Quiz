package ru.kpfu.itis.paramonov.local_database_api.domain.model.question

data class AnswerModel(
    val text: String,
    val isCorrect: Boolean
)