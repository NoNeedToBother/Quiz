package ru.kpfu.itis.paramonov.feature_users.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import javax.inject.Inject

class UserModelMapper @Inject constructor(): ModelMapper<FirebaseUser, UserModel> {
    override fun map(model: FirebaseUser): UserModel {
        return UserModel(
            id = model.id,
            username = model.username,
            profilePictureUrl = model.profilePictureUrl,
            info = model.info,
            dateRegistered = model.dateRegistered
        )
    }
}