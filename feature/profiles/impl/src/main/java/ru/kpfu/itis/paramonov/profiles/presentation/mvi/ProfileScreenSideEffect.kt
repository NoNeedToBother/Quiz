package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import android.net.Uri
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel

sealed class ProfileScreenSideEffect {
    data class ShowError(val title: String, val message: String): ProfileScreenSideEffect()
    data class ResultsReceived(val results: List<ResultUiModel>): ProfileScreenSideEffect()
    data object GoToSignIn: ProfileScreenSideEffect()
    data class FriendRequestsReceived(val requests: List<UserModel>): ProfileScreenSideEffect()
    data object CredentialsConfirmed: ProfileScreenSideEffect()
    data class ProfilePictureConfirmed(val uri: Uri): ProfileScreenSideEffect()
}
