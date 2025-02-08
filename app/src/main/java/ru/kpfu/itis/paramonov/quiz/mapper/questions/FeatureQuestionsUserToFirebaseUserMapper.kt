package ru.kpfu.itis.paramonov.quiz.mapper.questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.model.FirebaseUser
import ru.kpfu.itis.paramonov.questions.api.model.User

class FeatureQuestionsUserToFirebaseUserMapper: ModelMapper<User, FirebaseUser> {
    override fun map(model: User): FirebaseUser {
        return FirebaseUser(
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
