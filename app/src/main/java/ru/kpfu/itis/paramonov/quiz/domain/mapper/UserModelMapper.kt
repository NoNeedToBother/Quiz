package ru.kpfu.itis.paramonov.quiz.domain.mapper

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUserDomainModel
import ru.kpfu.itis.paramonov.quiz.presentation.model.UserModel
import javax.inject.Inject

class UserModelMapper @Inject constructor() {
    fun fromFirebaseDomainModel(firebaseUser: FirebaseUserDomainModel): UserModel {
        return UserModel(
            firebaseUser.username
        )
    }
}