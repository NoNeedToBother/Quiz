package ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi

sealed class RegisterScreenSideEffect {
    data object NavigateToMainMenu: RegisterScreenSideEffect()
    data class ShowError(val title: String, val message: String): RegisterScreenSideEffect()
}
