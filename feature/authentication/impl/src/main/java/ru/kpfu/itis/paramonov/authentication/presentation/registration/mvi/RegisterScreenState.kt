package ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class RegisterScreenState(
    val userData: UserModel?,
    val isLoading: Boolean = false,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null
)
