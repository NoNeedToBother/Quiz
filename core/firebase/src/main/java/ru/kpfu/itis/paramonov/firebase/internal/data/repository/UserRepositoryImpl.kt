package ru.kpfu.itis.paramonov.firebase.internal.data.repository

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.core.validators.Validator
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.CredentialException
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.UserNotAuthorizedException
import ru.kpfu.itis.paramonov.firebase.external.domain.utils.UpdateKeys
import ru.kpfu.itis.paramonov.firebase.internal.data.utils.waitResult
import java.lang.NullPointerException

internal class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
    private val passwordValidator: PasswordValidator,
    private val usernameValidator: UsernameValidator
): UserRepository {

    override suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser {
        auth.currentUser?.let {
            val userDocument = database.collection(USERS_COLLECTION_NAME).document(it.uid)
            val updates = mutableMapOf<String, Any>()
            for (pair in pairs) {
                val key = pair.first
                val value = pair.second

                when(key) {
                    UpdateKeys.UPDATE_ID_KEY -> updates[DB_ID_FIELD] = value
                    UpdateKeys.UPDATE_USERNAME_KEY -> {
                        (value as String).let { u ->
                            validateParameter(usernameValidator, u)
                            updates[DB_USERNAME_FIELD] = u
                        }
                    }
                    UpdateKeys.UPDATE_PROFILE_PICTURE_KEY -> {
                        updates[DB_PROFILE_PICTURE_FIELD] = when(value) {
                            is Uri -> processProfilePictureUri(it.uid, value)
                            else -> value
                        }
                    }
                    UpdateKeys.UPDATE_INFO_KEY -> updates[DB_INFO_FIELD] = value
                    UpdateKeys.UPDATE_DATE_REGISTERED_KEY -> updates[DB_DATE_REGISTERED_FIELD] = value
                }
            }

            return withContext(dispatcher) {
                val result = userDocument.set(updates, SetOptions.mergeFields(pairs.map {
                    pair -> pair.first
                })).waitResult()
                if (result.isSuccessful) {
                    getCurrentUser() ?: throw UserDataException(
                        resourceManager.getString(R.string.update_failed)
                    )
                }
                else throw UserDataException(
                    resourceManager.getString(R.string.update_failed)
                )
            }
        } ?: throw UserNotAuthorizedException(
            resourceManager.getString(R.string.not_authorized)
        )
    }

    private fun validateParameter(validator: Validator, param: String) {
        if (!validator.validate(param)) throw UserDataException(
            validator.getRequirements()
        )
    }

    override suspend fun updateCredentials(email: String?, password: String?) {
        password?.let { validateParameter(passwordValidator, it) }
        withContext(dispatcher) {
            val onFailure: () -> Unit = {
                throw CredentialException(resourceManager.getString(R.string.credential_update_failed))
            }
            auth.currentUser?.let { user ->
                val prevEmail = user.email!!
                if (email != null && password != null) {
                    val task = user.updatePassword(password).waitResult()
                    if (task.isSuccessful) {
                        val credential = EmailAuthProvider.getCredential(prevEmail, password)
                        user.reauthenticate(credential).waitResult().apply {
                            if (isSuccessful) user.updateEmail(email).waitResult()
                        }
                    } else {
                        onFailure.invoke()
                    }
                } else {
                    email?.let {
                        val task = user.updateEmail(email).waitResult()
                        if (!task.isSuccessful) onFailure.invoke()
                    }
                    password?.let {
                        val task = user.updatePassword(password).waitResult()
                        if (!task.isSuccessful) onFailure.invoke()
                    }
                }
            }
        }
    }

    override suspend fun reauthenticate(email: String, password: String) {
        withContext(dispatcher) {
            auth.currentUser?.let {
                val credential = EmailAuthProvider.getCredential(email, password)
                try {
                    val task = it.reauthenticate(credential).waitResult()
                    if (!task.isSuccessful) throw CredentialException(
                        resourceManager.getString(R.string.incorrect_credentials)
                    )
                } catch (ex: Throwable) {
                    throw CredentialException(
                        resourceManager.getString(R.string.incorrect_credentials)
                    )
                }
            }
        }
    }

    private suspend fun processProfilePictureUri(id: String, uri: Uri): String {
        return withContext(dispatcher) {
            storage.reference.child(
                String.format(PROFILE_PICTURE_STORAGE_REF, id)
            ).let { ref ->
                val result = ref.putFile(uri).waitResult()
                if (result.isSuccessful) {
                    ref.downloadUrl.waitResult().result.toString()
                } else {
                    throw UserDataException(
                        resourceManager.getString(R.string.update_failed)
                    )
                }
            }
        }
    }

    override suspend fun logoutUser(onLogoutSuccess: suspend () -> Unit) {
        val scope = CoroutineScope(dispatcher)
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                scope.launch {
                    onLogoutSuccess.invoke()
                }
            }
        }
        auth.signOut()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser?.run {
            getUser(uid)
        }
    }

    override suspend fun getUser(id: String): FirebaseUser? {
        val data = withContext(dispatcher) {
            database.collection(USERS_COLLECTION_NAME).document(id)
                .get().waitResult()
        }
        return if (data.isSuccessful) {
            try {
                data.result.getUser()
            } catch (ex: NullPointerException) {
                throw UserDataException(
                    resourceManager.getString(R.string.fail_read_user_data)
                )
            }
        } else null
    }

    override suspend fun getFriends(offset: Int, max: Int): List<FirebaseUser> {
        return withContext(dispatcher) {
            val user = getCurrentUser() ?: throw UserDataException(
                resourceManager.getString(R.string.fail_read_user_data)
            )
            val friendIdList = user.friendIdList
            if (offset > friendIdList.size) return@withContext listOf()
            val last = if (offset + max > friendIdList.size) friendIdList.size else offset + max
            val friendIdListWithOffset = friendIdList.subList(offset, last)
            val result = mutableListOf<FirebaseUser>()
            friendIdListWithOffset.forEach { id ->
                getUser(id)?.let { user -> result.add(user)}
            }
            result
        }
    }

    override suspend fun subscribeToProfileUpdates(): Flow<FirebaseUser> {
        return withContext(dispatcher) {
            getCurrentUser()?.let { user ->
                callbackFlow {
                    val listener = EventListener<DocumentSnapshot> { value, _ ->
                            value?.let {
                                launch {
                                    try { send(value.getUser())
                                    } catch (_: Throwable) {}
                                }
                            }
                        }
                    val registration = database.collection(USERS_COLLECTION_NAME)
                        .document(user.id).addSnapshotListener(listener)
                    awaitClose {
                        registration.remove()
                    }
                }
            } ?: throw UserNotAuthorizedException(
                resourceManager.getString(R.string.not_authorized)
            )
        }
    }

    override suspend fun findByUsername(username: String, max: Int, lastId: String?): List<FirebaseUser> {
        return withContext(dispatcher) {
            var query = database.collection(USERS_COLLECTION_NAME)
                .orderBy(DB_ID_FIELD)
                .whereGreaterThanOrEqualTo(DB_USERNAME_FIELD, username)
                .whereLessThanOrEqualTo(DB_USERNAME_FIELD, "$username~")
            lastId?.let {
                query = query
                    .whereGreaterThan(DB_ID_FIELD, it)
            }
            val result = query
                .limit(max.toLong())
                .get()
                .waitResult()

            if (result.isSuccessful) {
                val res = mutableListOf<FirebaseUser>()
                val documents = result.result.documents
                for (doc in documents) {
                    try {
                        res.add(doc.getUser())
                    } catch (_: Throwable) {}
                }
                res
            } else {
                throw UserDataException(
                    resourceManager.getString(R.string.find_users_error)
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun DocumentSnapshot.getUser(): FirebaseUser {
        val id = data?.get(DB_ID_FIELD) as String
        val username = data?.get(DB_USERNAME_FIELD) as String
        val profilePicture = data?.get(DB_PROFILE_PICTURE_FIELD) as String
        val info = data?.get(DB_INFO_FIELD) as String
        val dateRegistered = data?.get(DB_DATE_REGISTERED_FIELD) as String
        val friends = data?.get(DB_FRIENDS_FIELD) as List<String>? ?: listOf()
        val requestsFrom = data?.get(DB_REQUESTS_FIELD) as List<String>? ?: listOf()
        return FirebaseUser(
            id, username, profilePicture, info, dateRegistered, friends, requestsFrom
        )
    }

    companion object {
        private const val USERS_COLLECTION_NAME = "users"
        private const val DB_ID_FIELD = "id"
        private const val DB_USERNAME_FIELD = "username"
        private const val DB_PROFILE_PICTURE_FIELD = "profilePicture"
        private const val DB_INFO_FIELD = "info"
        private const val DB_DATE_REGISTERED_FIELD = "dateRegistered"
        private const val DB_FRIENDS_FIELD = "friends"
        private const val DB_REQUESTS_FIELD = "requestsFrom"

        private const val PROFILE_PICTURE_STORAGE_REF = "profiles/%s.png"
    }
}
