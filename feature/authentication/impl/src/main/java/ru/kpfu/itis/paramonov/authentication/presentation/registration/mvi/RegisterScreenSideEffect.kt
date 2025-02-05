package ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi

sealed class RegisterScreenSideEffect {
    data object NavigateToMainMenu: RegisterScreenSideEffect()
    data class ShowError(val message: String): RegisterScreenSideEffect()
}