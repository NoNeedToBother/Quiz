package ru.kpfu.itis.paramonov.questions.presentation.settings.mvi

sealed class QuestionSettingsScreenSideEffect {
    data class ShowError(val title: String, val message: String): QuestionSettingsScreenSideEffect()
}
