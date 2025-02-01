package ru.kpfu.itis.paramonov.quiz.mapper.feature_questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.questions.api.model.User
import javax.inject.Inject

class FeatureQuestionsUserToFirebaseUserMapper @Inject constructor(): ModelMapper<User, FirebaseUser> {
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