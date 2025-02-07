package ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi

sealed class SignInScreenSideEffect {
    data object NavigateToMainMenu: SignInScreenSideEffect()
    data class ShowError(val title: String, val message: String): SignInScreenSideEffect()
}