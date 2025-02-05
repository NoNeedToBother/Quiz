package ru.kpfu.itis.paramonov.questions.presentation.questions.mvi

import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel

data class TrainingQuestionsScreenState(
    val questions: List<QuestionDataUiModel> = emptyList()
)