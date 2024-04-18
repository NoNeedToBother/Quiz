package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser

interface UserRepository {
    suspend fun registerUser(username: String, email: String, password: String, confirmPassword: String): FirebaseUser

    suspend fun authenticateUser(email: String, password: String): FirebaseUser

    suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser

    suspend fun checkUserIsAuthenticated(): Boolean

    suspend fun logoutUser()
}