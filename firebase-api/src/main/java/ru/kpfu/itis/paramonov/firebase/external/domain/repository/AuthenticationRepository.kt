package ru.kpfu.itis.paramonov.firebase.external.domain.repository

import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser

interface AuthenticationRepository {

    suspend fun registerUser(username: String, email: String, password: String, confirmPassword: String): FirebaseUser

    suspend fun authenticateUser(email: String, password: String): FirebaseUser

    suspend fun checkUserIsAuthenticated(): FirebaseUser?
}