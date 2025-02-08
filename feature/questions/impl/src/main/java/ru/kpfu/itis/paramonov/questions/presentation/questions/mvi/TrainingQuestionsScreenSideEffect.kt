package ru.kpfu.itis.paramonov.questions.presentation.questions.mvi

sealed class TrainingQuestionsScreenSideEffect {
    data class ShowError(val title: String, val message: String): TrainingQuestionsScreenSideEffect()
}
