package ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import javax.inject.Inject

class UserUiModelMapper @Inject constructor(): ModelMapper<FirebaseUser, UserModel> {
    override fun map(model: FirebaseUser): UserModel {
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