package ru.kpfu.itis.paramonov.profiles.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.profiles.api.model.FriendStatus
import ru.kpfu.itis.paramonov.profiles.presentation.model.FriendStatusUiModel
import javax.inject.Inject

class FriendStatusUiModelMapper @Inject constructor(): ModelMapper<FriendStatus, FriendStatusUiModel> {
    override fun map(model: FriendStatus): FriendStatusUiModel {
        return when(model) {
            FriendStatus.FRIEND -> FriendStatusUiModel.FRIEND
            FriendStatus.SAME_USER -> FriendStatusUiModel.SAME_USER
            FriendStatus.REQUEST_SENT -> FriendStatusUiModel.REQUEST_SENT
            FriendStatus.NOT_FRIEND -> FriendStatusUiModel.NOT_FRIEND
        }
    }
}