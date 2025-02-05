package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class ProfileScreenState(
    val user: UserModel? = null,
    val processingCredentials: Boolean = false,
)