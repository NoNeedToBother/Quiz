package ru.kpfu.itis.paramonov.firebase.data.handler

import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ru.kpfu.itis.paramonov.common.handler.ExceptionHandler
import ru.kpfu.itis.paramonov.common.utils.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import ru.kpfu.itis.paramonov.quiz.R
import javax.inject.Inject

class RegistrationExceptionHandler @Inject constructor(
    private val resManager: ResourceManager
): ExceptionHandler {

    override fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthWeakPasswordException -> RegisterException(
                resManager.getString(R.string.weak_password)
            )
            is FirebaseAuthInvalidCredentialsException -> RegisterException(
                resManager.getString(R.string.invalid_email)
            )
            is FirebaseAuthUserCollisionException -> RegisterException(
                resManager.getString(R.string.taken_email)
            )
            else -> {
                RegisterException(
                    resManager.getString(R.string.register_fail_try_again)
                )
            }
        }
    }
}