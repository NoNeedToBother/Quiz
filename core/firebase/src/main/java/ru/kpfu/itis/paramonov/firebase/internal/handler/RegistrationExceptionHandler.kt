package ru.kpfu.itis.paramonov.firebase.internal.handler

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ru.kpfu.itis.paramonov.core.handler.ExceptionHandler
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.external.exceptions.RegisterException
import ru.kpfu.itis.paramonov.firebase.R

internal class RegistrationExceptionHandler(
    private val resourceManager: ResourceManager
): ExceptionHandler {

    override fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthWeakPasswordException -> RegisterException(
                resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.weak_password)
            )
            is FirebaseAuthInvalidCredentialsException -> RegisterException(
                resourceManager.getString(R.string.invalid_email)
            )
            is FirebaseAuthUserCollisionException -> RegisterException(
                resourceManager.getString(R.string.taken_email)
            )
            else -> {
                RegisterException(
                    resourceManager.getString(R.string.register_fail_try_again)
                )
            }
        }
    }
}
