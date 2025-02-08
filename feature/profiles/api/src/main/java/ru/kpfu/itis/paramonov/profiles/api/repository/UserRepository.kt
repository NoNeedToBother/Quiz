package ru.kpfu.itis.paramonov.profiles.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.paramonov.profiles.api.model.User

interface UserRepository {

    suspend fun getCurrentUser(): User?

    suspend fun getUser(id: String): User?

    suspend fun logoutUser(onLogoutSuccess: suspend () -> Unit)

    suspend fun reauthenticate(email: String, password: String)

    suspend fun subscribeToProfileUpdates(): Flow<User>

    suspend fun updateUser(vararg pairs: Pair<String, Any>): User

    suspend fun updateCredentials(email: String?, password: String?)

}
