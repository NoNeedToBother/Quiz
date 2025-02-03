package ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class SignInScreenState(
    val userData: UserModel?,
    val isLoading: Boolean = false,
    val isPasswordCorrect: Boolean = false,
    val isUsernameCorrect: Boolean = false,
)