package ru.kpfu.itis.paramonov.users.presentation.mvi

sealed class FriendsScreenSideEffect {
    data class ShowError(val title: String, val message: String): FriendsScreenSideEffect()
}