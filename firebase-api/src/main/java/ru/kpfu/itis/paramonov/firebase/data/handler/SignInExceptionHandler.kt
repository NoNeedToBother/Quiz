package ru.kpfu.itis.paramonov.firebase.data.handler

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ru.kpfu.itis.paramonov.common.handler.ExceptionHandler
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import ru.kpfu.itis.paramonov.firebase.R
import javax.inject.Inject

class SignInExceptionHandler @Inject constructor(
    private val resManager: ResourceManager
): ExceptionHandler {

    override fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthInvalidUserException -> SignInException(
                resManager.getString(R.string.user_email_exists)
            )
            is FirebaseAuthInvalidCredentialsException -> SignInException(
                resManager.getString(R.string.no_user_email)
            )
            else -> SignInException(
                resManager.getString(R.string.sign_in_fail_try_again)
            )
        }
    }
}