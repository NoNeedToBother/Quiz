package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel

sealed class OtherUserProfileScreenSideEffect {
    data class ShowError(val title: String, val message: String): OtherUserProfileScreenSideEffect()
    data class ResultsReceived(val results: List<ResultUiModel>): OtherUserProfileScreenSideEffect()
}
