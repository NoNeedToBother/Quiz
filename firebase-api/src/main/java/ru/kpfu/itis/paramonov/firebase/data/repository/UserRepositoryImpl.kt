package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.R
import java.lang.RuntimeException
import javax.inject.Inject

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
        return result.run {
            if (isSuccessful) {
                updateUser(USERNAME_KEY to username)
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
        return user?.displayName?.let {
            FirebaseUser(
                user.uid,
                it
            )
        } ?: throw RuntimeException(resManager.getString(R.string.fail_read_user_data))
    }

    override suspend fun checkUserIsAuthenticated(): Boolean {
        return auth.currentUser?.let {
            true
        } ?: false
    }

    private suspend fun signInUser(email: String, password: String): FirebaseUser {
        val result = withContext(dispatcher) {
            try {
                auth.signInWithEmailAndPassword(email, password).waitResult()
            } catch (ex: Exception) {
                throw signInExceptionHandler.handle(ex)
            }
        }
        return result.run {
            if (isSuccessful) {
                auth.currentUser?.run {
                    displayName?.let {
                        FirebaseUser(
                            uid,
                            it
                        )
                    }
                } ?: throw RuntimeException(resManager.getString(R.string.fail_read_user_data))
            } else {
                exception?.let { throw registerExceptionHandler.handle(it) } ?:
                throw SignInException(resManager.getString(R.string.sign_in_fail_try_again))
            }
        }
    }

    private suspend fun <T> Task<T>.waitResult(): Task<T> {
        while (true) {
            if (isComplete) break
            else delay(100L)
        }
        return this
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