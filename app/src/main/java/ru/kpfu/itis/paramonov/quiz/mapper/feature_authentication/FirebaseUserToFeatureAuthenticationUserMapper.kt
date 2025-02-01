package ru.kpfu.itis.paramonov.quiz.mapper.feature_authentication

import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import javax.inject.Inject

class FirebaseUserToFeatureAuthenticationUserMapper @Inject constructor(): ModelMapper<FirebaseUser, User> {
    override fun map(model: FirebaseUser): User {
        return User(
            id = model.id,
            username = model.username,
            profilePictureUrl = model.profilePictureUrl,
            info = model.info,
            dateRegistered = model.dateRegistered,
            friendIdList = model.friendIdList,
            friendRequestFromList = model.friendRequestFromList,
        )
    }
}