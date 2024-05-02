package ru.kpfu.itis.paramonov.firebase.data.repository

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.exceptions.CredentialException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserNotAuthorizedException
import ru.kpfu.itis.paramonov.firebase.data.utils.UpdateKeys
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import java.lang.NullPointerException
import java.util.Optional

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager
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
                    UpdateKeys.UPDATE_USERNAME_KEY -> updates[DB_USERNAME_FIELD] = value
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
                if (result.isSuccessful) getCurrentUser().get()
                else throw UserDataException(
                    resourceManager.getString(R.string.update_failed)
                )
            }
        } ?: throw UserNotAuthorizedException(
            resourceManager.getString(R.string.not_authorized)
        )
    }

    override suspend fun updateCredentials(email: String?, password: String?) {
        withContext(dispatcher) {
            val onFailure: () -> Unit = {
                throw CredentialException(resourceManager.getString(R.string.credential_update_failed))
            }
            auth.currentUser?.let { user ->
                val prevEmail = user.email!!
                if (email != null && password != null) {
                    withContext(dispatcher) {
                        val task = user.updatePassword(password).waitResult().addOnFailureListener {
                        }
                        if (task.isSuccessful) {
                            withContext(dispatcher) {
                                val credential = EmailAuthProvider.getCredential(prevEmail, password)
                                user.reauthenticate(credential).waitResult().apply {
                                    if (isSuccessful) user.updateEmail(email)
                                }

                            }
                        } else {
                            onFailure.invoke()
                        }
                    }
                } else {
                    email?.let {
                        withContext(dispatcher) {
                            val task = user.updateEmail(email).waitResult()
                            if (!task.isSuccessful) onFailure.invoke()
                        }
                    }
                    password?.let {
                        withContext(dispatcher) {
                            val task = user.updatePassword(password).waitResult()
                            if (!task.isSuccessful) onFailure.invoke()
                        }
                    }
                }
            }
        }
    }

    override suspend fun reauthenticate(email: String, password: String) {
        withContext(dispatcher) {
            auth.currentUser?.let {
                val credential = EmailAuthProvider.getCredential(email, password)
                withContext(dispatcher) {
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
        private const val USERS_COLLECTION_NAME = "users"
        private const val DB_ID_FIELD = "id"
        private const val DB_USERNAME_FIELD = "username"
        private const val DB_PROFILE_PICTURE_FIELD = "profilePicture"
        private const val DB_INFO_FIELD = "info"
        private const val DB_DATE_REGISTERED_FIELD = "dateRegistered"

        private const val PROFILE_PICTURE_STORAGE_REF = "profiles/%s.png"
    }
}