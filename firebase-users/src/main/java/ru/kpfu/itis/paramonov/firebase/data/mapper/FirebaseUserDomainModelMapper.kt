package ru.kpfu.itis.paramonov.firebase.data.mapper

import ru.kpfu.itis.paramonov.firebase.data.model.FirebaseUserDataModel
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUserDomainModel
import javax.inject.Inject

class FirebaseUserDomainModelMapper @Inject constructor() {
    fun fromFirebaseDataModel(firebaseUser: FirebaseUserDataModel): FirebaseUserDomainModel {
        return FirebaseUserDomainModel(
            firebaseUser.id,
            firebaseUser.username
        )
    }
}