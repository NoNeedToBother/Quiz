package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import java.util.Optional

interface UserRepository {
    suspend fun registerUser(username: String, email: String, password: String, confirmPassword: String): FirebaseUser

    suspend fun authenticateUser(email: String, password: String): FirebaseUser

    suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser

    suspend fun logoutUser()

    suspend fun getCurrentUser(): Optional<FirebaseUser>

    suspend fun getUser(id: String): Optional<FirebaseUser>
}