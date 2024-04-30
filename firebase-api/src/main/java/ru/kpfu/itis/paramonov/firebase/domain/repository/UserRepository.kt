package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import java.util.Optional

interface UserRepository {
    suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser

    suspend fun logoutUser()

    suspend fun getCurrentUser(): Optional<FirebaseUser>

    suspend fun getUser(id: String): Optional<FirebaseUser>

    suspend fun getDefaultProfilePicture(): String

    suspend fun getDefaultInfo(username: String): String
}