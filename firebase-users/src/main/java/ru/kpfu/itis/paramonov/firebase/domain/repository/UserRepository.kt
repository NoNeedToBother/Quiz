package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUserDomainModel

interface UserRepository {
    suspend fun registerUser(username: String, email: String, password: String): FirebaseUserDomainModel

    suspend fun authenticateUser(email: String, password: String): FirebaseUserDomainModel

    suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUserDomainModel
}