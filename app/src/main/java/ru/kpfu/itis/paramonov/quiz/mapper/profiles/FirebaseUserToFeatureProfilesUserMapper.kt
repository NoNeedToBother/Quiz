package ru.kpfu.itis.paramonov.quiz.mapper.profiles

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.profiles.api.model.User

class FirebaseUserToFeatureProfilesUserMapper: ModelMapper<FirebaseUser, User> {
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
