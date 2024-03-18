package ru.kpfu.itis.paramonov.firebase.data.handler

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.SignInException
import javax.inject.Inject

class SignInExceptionHandler @Inject constructor() {

    fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthInvalidUserException -> SignInException("No user with this email exists")
            is FirebaseAuthInvalidCredentialsException -> SignInException("No user with this email exists")
            else -> SignInException("Failed to sign in, try again")
        }
    }
}