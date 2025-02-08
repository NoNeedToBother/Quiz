package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class ProfileScreenState(
    val user: UserModel? = null,
    val processingCredentials: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val emailError: String? = null
)
