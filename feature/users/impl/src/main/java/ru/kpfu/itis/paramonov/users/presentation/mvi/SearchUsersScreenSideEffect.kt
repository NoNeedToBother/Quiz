package ru.kpfu.itis.paramonov.users.presentation.mvi

sealed class SearchUsersScreenSideEffect {
    data class ShowError(val title: String, val message: String): SearchUsersScreenSideEffect()
}