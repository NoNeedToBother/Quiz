package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserNotAuthorizedException
import ru.kpfu.itis.paramonov.firebase.data.utils.Keys
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import java.lang.NullPointerException
import java.util.Optional

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
    private val dateTimeParser: DateTimeParser
): UserRepository {

    override suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser {
        auth.currentUser?.let {
            val userDocument = database.collection(USERS_COLLECTION_NAME).document(it.uid)
            val updates = mutableMapOf<String, Any>()
            for (pair in pairs) {
                val key = pair.first
                val value = pair.second

                when(key) {
                    Keys.UPDATE_ID_KEY -> updates[DB_ID_FIELD] = value
                    Keys.UPDATE_USERNAME_KEY -> updates[DB_USERNAME_FIELD] = value
                    Keys.UPDATE_PROFILE_PICTURE_KEY -> updates[DB_PROFILE_PICTURE_FIELD] = value
                    Keys.UPDATE_INFO_KEY -> updates[DB_INFO_FIELD] = value
                    Keys.UPDATE_DATE_REGISTERED_KEY -> updates[DB_DATE_REGISTERED_FIELD] = value
                }
            }

            return withContext(dispatcher) {
                val result = userDocument.set(updates).waitResult()
                if (result.isSuccessful) getCurrentUser().get()
                else throw UserDataException(
                    resourceManager.getString(R.string.update_failed)
                )
            }
        } ?: throw UserNotAuthorizedException(
            resourceManager.getString(R.string.not_authorized)
        )
    }

    override suspend fun logoutUser(onLogoutSuccess: () -> Unit) {
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                onLogoutSuccess.invoke()
            }
        }
        auth.signOut()
    }

    override suspend fun getCurrentUser(): Optional<FirebaseUser> {
        return auth.currentUser?.run {
            getUser(uid)
        } ?: Optional.empty()
    }

    override suspend fun getUser(id: String): Optional<FirebaseUser> {
        val data = withContext(dispatcher) {
            database.collection(USERS_COLLECTION_NAME).document(id)
                .get().waitResult()
        }
        return if (data.isSuccessful) {
            try {
                Optional.of(data.result.getUser())
            } catch (ex: NullPointerException) {
                throw UserDataException(
                    resourceManager.getString(R.string.corrupted_data)
                )
            }
        } else Optional.empty()
    }

    override suspend fun getDefaultProfilePicture(): String = DEFAULT_PROFILE_PICTURE_URL

    override suspend fun getDefaultInfo(username: String): String =
        String.format(DEFAULT_INFO, username)

    private fun DocumentSnapshot.getUser(): FirebaseUser {
        val id = data?.get(DB_ID_FIELD) as String
        val username = data?.get(DB_USERNAME_FIELD) as String
        val profilePicture = data?.get(DB_PROFILE_PICTURE_FIELD) as String
        val info = data?.get(DB_INFO_FIELD) as String
        val dateRegistered = data?.get(DB_DATE_REGISTERED_FIELD) as String
        return FirebaseUser(
            id, username, profilePicture, info, dateRegistered
        )
    }

    companion object {
        private const val DEFAULT_PROFILE_PICTURE_URL = ""
        private const val DEFAULT_INFO = "Hello this is %s"

        private const val USERS_COLLECTION_NAME = "users"
        private const val DB_ID_FIELD = "id"
        private const val DB_USERNAME_FIELD = "username"
        private const val DB_PROFILE_PICTURE_FIELD = "profilePicture"
        private const val DB_INFO_FIELD = "info"
        private const val DB_DATE_REGISTERED_FIELD = "dateRegistered"
    }
}