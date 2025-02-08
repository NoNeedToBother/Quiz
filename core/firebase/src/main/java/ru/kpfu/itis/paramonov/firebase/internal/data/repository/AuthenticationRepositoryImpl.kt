package ru.kpfu.itis.paramonov.firebase.internal.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.core.validators.Validator
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.CredentialException
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.internal.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.internal.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.external.domain.utils.UpdateKeys
import ru.kpfu.itis.paramonov.firebase.internal.data.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.external.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository

internal class AuthenticationRepositoryImpl(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
    private val registerExceptionHandler: RegistrationExceptionHandler,
    private val signInExceptionHandler: SignInExceptionHandler,
    private val resourceManager: ResourceManager,
    private val userRepository: UserRepository,
    private val dateTimeParser: DateTimeParser,
    private val passwordValidator: PasswordValidator,
    private val usernameValidator: UsernameValidator
): AuthenticationRepository {

    override suspend fun registerUser
                (username: String,
                 email: String,
                 password: String,
                 confirmPassword: String
    ): FirebaseUser {
        validateRegistrationParameter(passwordValidator, password)
        validateRegistrationParameter(usernameValidator, username)
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
                        UpdateKeys.UPDATE_ID_KEY to it.uid,
                        UpdateKeys.UPDATE_USERNAME_KEY to username,
                        UpdateKeys.UPDATE_PROFILE_PICTURE_KEY to DEFAULT_PROFILE_PICTURE_URL,
                        UpdateKeys.UPDATE_INFO_KEY to getDefaultInfo(username),
                        UpdateKeys.UPDATE_DATE_REGISTERED_KEY
                                to dateTimeParser.parseMillisToString(System.currentTimeMillis())
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
                user?.let {
                    return it
                } ?: throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            } else {
                exception?.let { throw signInExceptionHandler.handle(it) } ?:
                throw SignInException(resourceManager.getString(R.string.sign_in_fail_try_again))
            }
        }
    }

    override suspend fun checkUserIsAuthenticated(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }

    private fun getDefaultInfo(username: String): String = String.format(DEFAULT_INFO, username)

    private fun validateRegistrationParameter(validator: Validator, param: String) {
        if (!validator.validate(param)) throw CredentialException(
            validator.getRequirements()
        )
    }

    companion object {
        private const val DEFAULT_PROFILE_PICTURE_URL =
            "https://firebasestorage.googleapis.com/v0/b/quiz-6001c.appspot.com/o/" +
                    "profiles%2Fdefault.png?alt=media&token=52efa4e4-9779-4389-b8f4-61aa48004a48"
        private const val DEFAULT_INFO = "Hello this is %s"
    }
}
