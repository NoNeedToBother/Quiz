package ru.kpfu.itis.paramonov.profiles.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.api.model.User

class UserUiModelMapper: ModelMapper<User, UserModel> {
    override fun map(model: User): UserModel {
        return UserModel(
            id = model.id,
            username = model.username,
            profilePictureUrl = model.profilePictureUrl,
            info = model.info,
            dateRegistered = model.dateRegistered,
            friendIdList = model.friendIdList,
            friendRequestFromList = model.friendRequestFromList
        )
    }
}