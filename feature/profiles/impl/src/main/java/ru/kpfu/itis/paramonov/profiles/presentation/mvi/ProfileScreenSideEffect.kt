package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel

sealed class ProfileScreenSideEffect {
    data class ShowError(val message: String): ProfileScreenSideEffect()
    data class ResultsReceived(val results: List<ResultUiModel>): ProfileScreenSideEffect()
    data object GoToSignIn: ProfileScreenSideEffect()
    data class FriendRequestsReceived(val requests: List<UserModel>): ProfileScreenSideEffect()
    data object CredentialsConfirmed: ProfileScreenSideEffect()
}