package ru.kpfu.itis.paramonov.firebase.internal.data.handler

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ru.kpfu.itis.paramonov.core.handler.ExceptionHandler
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.R

internal class SignInExceptionHandler(
    private val resourceManager: ResourceManager
): ExceptionHandler {

    override fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthInvalidUserException -> SignInException(
                resourceManager.getString(R.string.user_email_exists)
            )
            is FirebaseAuthInvalidCredentialsException -> SignInException(
                resourceManager.getString(R.string.incorrect_credentials)
            )
            else -> SignInException(
                resourceManager.getString(R.string.sign_in_fail_try_again)
            )
        }
    }
}