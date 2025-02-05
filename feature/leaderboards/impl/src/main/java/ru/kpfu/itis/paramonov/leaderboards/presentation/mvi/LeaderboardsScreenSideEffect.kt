package ru.kpfu.itis.paramonov.leaderboards.presentation.mvi

sealed class LeaderboardsScreenSideEffect {
    data class ShowError(val message: String): LeaderboardsScreenSideEffect()
}