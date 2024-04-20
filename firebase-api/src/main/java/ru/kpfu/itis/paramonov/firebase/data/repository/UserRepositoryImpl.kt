package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import java.lang.RuntimeException
import java.util.Optional

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
    private val registerExceptionHandler: RegistrationExceptionHandler,
    private val signInExceptionHandler: SignInExceptionHandler,
    private val resManager: ResourceManager
): UserRepository {
    override suspend fun registerUser
                (username: String,
                 email: String,
                 password: String,
                 confirmPassword: String
    ): FirebaseUser {
        if (!checkPassword(password)) throw RegisterException(
            resManager.getString(R.string.weak_password)
        )
        if (confirmPassword != password) throw RegisterException(
            resManager.getString(R.string.passwords_not_match)
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
                return updateUser(USERNAME_KEY to username)
            } else {
                throw exception?.let { registerExceptionHandler.handle(it) } ?:
                throw RegisterException(resManager.getString(R.string.register_fail_try_again))
            }
        }
    }

    override suspend fun authenticateUser(email: String, password: String): FirebaseUser {
        val user = auth.currentUser
        return user?.run {
            user.displayName?.run {
                FirebaseUser(user.uid, this)
            } ?: throw RuntimeException(resManager.getString(R.string.fail_read_user_data))
        } ?: signInUser(email, password)
    }

    override suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUser {
        val user = updateUserProfile(pairs)

        return user?.displayName?.let {
            FirebaseUser(
                user.uid,
                it
            )
        } ?: throw RuntimeException(resManager.getString(R.string.fail_read_user_data))
    }

    private suspend fun updateUserProfile(pairs: Array<out Pair<String, Any>>): com.google.firebase.auth.FirebaseUser? {
        val builder = UserProfileChangeRequest.Builder()
        for (pair in pairs) {
            val param = pair.first
            val value = pair.second

            when(param) {
                USERNAME_KEY -> builder.displayName = value as String
            }
        }

        val user = auth.currentUser
        user?.updateProfile(builder.build())?.waitResult()
        return auth.currentUser
    }

    override suspend fun logoutUser() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): Optional<FirebaseUser> {
        return auth.currentUser?.run {
            displayName?.let {
                val user = FirebaseUser(uid, it)
                Optional.of(user)
            } ?: throw RuntimeException(resManager.getString(R.string.fail_read_user_data))
        } ?: Optional.empty()
    }

    private suspend fun signInUser(email: String, password: String): FirebaseUser {
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
                } else throw SignInException(resManager.getString(R.string.sign_in_fail_try_again))
            } else {
                exception?.let { throw registerExceptionHandler.handle(it) } ?:
                throw SignInException(resManager.getString(R.string.sign_in_fail_try_again))
            }
        }
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
        private const val USERNAME_KEY = "username"
    }
}