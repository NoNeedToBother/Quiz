package ru.kpfu.itis.paramonov.questions.presentation.questions.mvi

import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.ResultDataUiModel

data class QuestionsScreenState(
    val questions: List<QuestionDataUiModel> = emptyList(),
    val time: Int = 0,
    val maxScore: Double = -1.0,
    val result: ResultDataUiModel? = null
)
