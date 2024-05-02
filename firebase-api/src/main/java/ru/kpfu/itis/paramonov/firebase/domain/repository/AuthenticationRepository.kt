package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import java.util.Optional

interface AuthenticationRepository {

    suspend fun registerUser(username: String, email: String, password: String, confirmPassword: String): FirebaseUser

    suspend fun authenticateUser(email: String, password: String): FirebaseUser

    suspend fun checkUserIsAuthenticated(): Optional<FirebaseUser>
}