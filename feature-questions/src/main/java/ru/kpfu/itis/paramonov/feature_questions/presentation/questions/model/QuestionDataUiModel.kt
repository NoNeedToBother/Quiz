package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model

data class QuestionDataUiModel(
    val text: String,
    val answer: String,
    val incorrectAnswers: List<String>
)