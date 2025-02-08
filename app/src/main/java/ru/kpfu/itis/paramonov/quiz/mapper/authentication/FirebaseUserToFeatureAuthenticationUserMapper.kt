package ru.kpfu.itis.paramonov.quiz.mapper.authentication

import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.model.FirebaseUser

class FirebaseUserToFeatureAuthenticationUserMapper: ModelMapper<FirebaseUser, User> {
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
