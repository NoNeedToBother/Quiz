package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.utils.Keys
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import java.util.Optional

class AuthenticationRepositoryImpl(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
    private val registerExceptionHandler: RegistrationExceptionHandler,
    private val signInExceptionHandler: SignInExceptionHandler,
    private val resourceManager: ResourceManager,
    private val userRepository: UserRepository
): AuthenticationRepository {

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
                    return userRepository.updateUser(
                        Keys.UPDATE_ID_KEY to it.uid,
                        Keys.UPDATE_USERNAME_KEY to username,
                        Keys.UPDATE_PROFILE_PICTURE_KEY to userRepository.getDefaultProfilePicture(),
                        Keys.UPDATE_INFO_KEY to userRepository.getDefaultInfo(username)
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
                val user = userRepository.getCurrentUser()
                if (user.isPresent) {
                    return user.get()
                } else throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            } else {
                exception?.let { throw registerExceptionHandler.handle(it) } ?:
                throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            }
        }
    }

    override suspend fun checkUserIsAuthenticated(): Optional<FirebaseUser> {
        return userRepository.getCurrentUser()
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
}