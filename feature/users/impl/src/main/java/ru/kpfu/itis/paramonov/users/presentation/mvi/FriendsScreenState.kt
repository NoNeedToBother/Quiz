package ru.kpfu.itis.paramonov.users.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

data class FriendsScreenState(
    val friends: List<UserModel> = emptyList(),
    val loadingEnded: Boolean = false,
    val pagedFriends: List<UserModel> = emptyList()
)