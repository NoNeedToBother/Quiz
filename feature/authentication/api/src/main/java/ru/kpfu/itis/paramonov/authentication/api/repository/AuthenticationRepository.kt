package ru.kpfu.itis.paramonov.authentication.api.repository

import ru.kpfu.itis.paramonov.authentication.api.model.User

interface AuthenticationRepository {

    suspend fun registerUser(username: String, email: String, password: String, confirmPassword: String): User

    suspend fun authenticateUser(email: String, password: String): User

    suspend fun checkUserIsAuthenticated(): User?

}