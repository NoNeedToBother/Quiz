package ru.kpfu.itis.paramonov.network.external.domain.model

data class QuestionDataDomainModel(
    val text: String,
    val answer: String,
    val incorrectAnswers: List<String>
)