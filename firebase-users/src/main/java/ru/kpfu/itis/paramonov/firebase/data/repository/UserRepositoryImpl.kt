package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.mapper.FirebaseUserDomainModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUserDomainModel
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val mapper: FirebaseUserDomainModelMapper,
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
    private val registerExceptionHandler: RegistrationExceptionHandler,
    private val signInExceptionHandler: SignInExceptionHandler
): UserRepository {
    override suspend fun registerUser
                (username: String,
                 email: String,
                 password: String): FirebaseUserDomainModel {
        try {
            val result = withContext(dispatcher) {
                auth.createUserWithEmailAndPassword(email, password).waitResult()
            }

            return result.run {
                if (isSuccessful) {
                    updateUser("username" to username)
                } else throw RegisterException("Failed to register, try again")
            }
        } catch (ex: Exception) {
            throw registerExceptionHandler.handle(ex)
        }
    }

    override suspend fun authenticateUser(email: String, password: String): FirebaseUserDomainModel {
        val user = auth.currentUser
        return user?.run {
            user.displayName?.run {
                FirebaseUserDomainModel(this)
            } ?: FirebaseUserDomainModel("")
        } ?: signInUser(email, password)
    }

    override suspend fun updateUser(vararg pairs: Pair<String, Any>): FirebaseUserDomainModel {
        val builder = UserProfileChangeRequest.Builder()
        for (pair in pairs) {
            val param = pair.first
            val value = pair.second

            when(param) {
                "username" -> builder.displayName = value as String
            }
        }

        val user = auth.currentUser
        user?.updateProfile(builder.build())?.waitResult()
        return user?.displayName?.let {
            FirebaseUserDomainModel(
                it
            )
        } ?: FirebaseUserDomainModel("")
    }

    private suspend fun signInUser(email: String, password: String): FirebaseUserDomainModel {
        try {
            val result = withContext(dispatcher) {
                auth.signInWithEmailAndPassword(email, password).waitResult()
            }
            return result.run {
                if (isSuccessful) {
                    auth.currentUser?.displayName?.let {
                        FirebaseUserDomainModel(
                            it
                        )
                    } ?: FirebaseUserDomainModel("")
                } else throw SignInException("Failed to sign in, try again later")
            }
        } catch (ex: Exception) {
            throw signInExceptionHandler.handle(ex)
        }
    }

    private suspend fun <T> Task<T>.waitResult(): Task<T> {
        while (true) {
            if (isComplete) break
            else delay(100L)
        }
        return this
    }
}