package ru.kpfu.itis.paramonov.users.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class SearchUsersScreenState(
    val users: List<UserModel> = emptyList(),
    val loadingEnded: Boolean = false
)
