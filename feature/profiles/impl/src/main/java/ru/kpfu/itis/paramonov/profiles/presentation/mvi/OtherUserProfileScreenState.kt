package ru.kpfu.itis.paramonov.profiles.presentation.mvi

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.presentation.model.FriendStatusUiModel

data class OtherUserProfileScreenState(
    val user: UserModel? = null,
    val friendRequestSent: Boolean? = null,
    val friendStatus: FriendStatusUiModel? = null
)