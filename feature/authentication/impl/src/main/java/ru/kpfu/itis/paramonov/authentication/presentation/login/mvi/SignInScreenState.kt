package ru.kpfu.itis.paramonov.authentication.presentation.login.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class SignInScreenState(
    val userData: UserModel?,
    val isLoading: Boolean = false,
    val passwordError: String? = null,
    val emailError: String? = null,
)
