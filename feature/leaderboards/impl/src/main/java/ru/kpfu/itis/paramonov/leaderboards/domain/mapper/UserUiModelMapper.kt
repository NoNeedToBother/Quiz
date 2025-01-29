package ru.kpfu.itis.paramonov.leaderboards.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.leaderboards.api.model.User
import javax.inject.Inject

class UserUiModelMapper @Inject constructor(): ModelMapper<User, UserModel> {
    override fun map(model: User): UserModel {
        return UserModel(
            id = model.id,
            username = model.username,
            profilePictureUrl = model.profilePictureUrl,
            dateRegistered = model.dateRegistered,
            info = model.info,
            friendIdList = model.friendIdList,
            friendRequestFromList = model.friendRequestFromList
        )
    }
}