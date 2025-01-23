package ru.kpfu.itis.paramonov.firebase.external.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser

interface UserRepository {
    suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser

    suspend fun updateCredentials(email: String?, password: String?)

    suspend fun reauthenticate(email: String, password: String)

    suspend fun logoutUser(onLogoutSuccess: () -> Unit)

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun getUser(id: String): FirebaseUser?

    suspend fun getFriends(offset: Int, max: Int): List<FirebaseUser>

    suspend fun subscribeToProfileUpdates(): Flow<FirebaseUser>

    suspend fun findByUsername(username: String, max: Int, lastId: String?): List<FirebaseUser>
}