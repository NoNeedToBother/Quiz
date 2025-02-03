package ru.kpfu.itis.paramonov.users.presentation.mvi

sealed class FriendsSideEffect {
    data class ShowError(val message: String): FriendsSideEffect()
}