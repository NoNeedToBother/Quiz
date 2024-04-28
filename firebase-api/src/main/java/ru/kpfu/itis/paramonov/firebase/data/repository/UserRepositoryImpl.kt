package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserNotAuthorizedException
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.utils.getUser
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import java.lang.NullPointerException
import java.util.Optional

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val registerExceptionHandler: RegistrationExceptionHandler,
    private val signInExceptionHandler: SignInExceptionHandler,
    private val resourceManager: ResourceManager
): UserRepository {

    override suspend fun registerUser
                (username: String,
                 email: String,
                 password: String,
                 confirmPassword: String
    ): FirebaseUser {
        if (!checkPassword(password)) throw RegisterException(
            resourceManager.getString(R.string.weak_password)
        )
        if (confirmPassword != password) throw RegisterException(
            resourceManager.getString(R.string.passwords_not_match)
        )
        val result = withContext(dispatcher) {
            try {
                auth.createUserWithEmailAndPassword(email, password).waitResult()
            } catch (ex: Exception) {
                throw registerExceptionHandler.handle(ex)
            }
        }
        result.run {
            if (isSuccessful) {
                this.result.user?.let {
                    return updateUser(
                        UPDATE_ID_KEY to it.uid,
                        UPDATE_USERNAME_KEY to username,
                        UPDATE_PROFILE_PICTURE_KEY to DEFAULT_PROFILE_PICTURE_URL,
                        UPDATE_INFO_KEY to String.format(DEFAULT_INFO, username)
                    )
                } ?: throw RegisterException(resourceManager.getString(R.string.register_fail_try_again))
            } else {
                throw exception?.let { registerExceptionHandler.handle(it) } ?:
                throw RegisterException(resourceManager.getString(R.string.register_fail_try_again))
            }
        }
    }

    override suspend fun authenticateUser(email: String, password: String): FirebaseUser {
        val result = withContext(dispatcher) {
            try {
                auth.signInWithEmailAndPassword(email, password).waitResult()
            } catch (ex: Exception) {
                throw signInExceptionHandler.handle(ex)
            }
        }
        result.run {
            if (isSuccessful) {
                val user = getCurrentUser()
                if (user.isPresent) {
                    return user.get()
                } else throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            } else {
                exception?.let { throw registerExceptionHandler.handle(it) } ?:
                throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            }
        }
    }

    override suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser {
        auth.currentUser?.let {
            val userDocument = database.collection(USERS_COLLECTION_NAME).document(it.uid)
            val updates = mutableMapOf<String, Any>()
            for (pair in pairs) {
                val key = pair.first
                val value = pair.second

                when(key) {
                    UPDATE_ID_KEY -> updates[DB_ID_FIELD] = value
                    UPDATE_USERNAME_KEY -> updates[DB_USERNAME_FIELD] = value
                    UPDATE_PROFILE_PICTURE_KEY -> updates[DB_PROFILE_PICTURE_FIELD] = value
                    UPDATE_INFO_KEY -> updates[DB_INFO_FIELD] = value
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

    override suspend fun logoutUser() {
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

    private fun checkPassword(password: String): Boolean {
        var hasDigit = false
        var hasUpperCase = false
        var hasLowerCase = false
        for (letter in password) {
            if (letter.isDigit()) hasDigit = true
            if (letter.isLowerCase()) hasLowerCase = true
            if (letter.isUpperCase()) hasUpperCase = true
        }
        return hasDigit && hasUpperCase && hasLowerCase
    }

    companion object {
        const val UPDATE_USERNAME_KEY = "username"
        private const val UPDATE_ID_KEY = "id"
        const val UPDATE_PROFILE_PICTURE_KEY = "profilePicture"
        const val UPDATE_INFO_KEY = "info"

        private const val DEFAULT_PROFILE_PICTURE_URL = ""
        private const val DEFAULT_INFO = "Hello this is %s"

        const val USERS_COLLECTION_NAME = "users"
        const val DB_ID_FIELD = "id"
        const val DB_USERNAME_FIELD = "username"
        const val DB_PROFILE_PICTURE_FIELD = "profilePicture"
        const val DB_INFO_FIELD = "info"
    }
}