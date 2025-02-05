package ru.kpfu.itis.paramonov.users.presentation.mvi

sealed class SearchUsersScreenSideEffect {
    data class ShowError(val message: String): SearchUsersScreenSideEffect()
}