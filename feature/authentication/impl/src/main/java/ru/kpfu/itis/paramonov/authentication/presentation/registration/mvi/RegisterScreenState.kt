package ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class RegisterScreenState(
    val userData: UserModel?,
    val isLoading: Boolean = false,
    val isPasswordCorrect: Boolean = false,
    val isConfirmPasswordCorrect: Boolean = false,
    val isUsernameCorrect: Boolean = false,
)