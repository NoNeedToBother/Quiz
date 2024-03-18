package ru.kpfu.itis.paramonov.firebase.data.handler

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ru.kpfu.itis.paramonov.firebase.data.exceptions.RegisterException
import javax.inject.Inject

class RegistrationExceptionHandler @Inject constructor() {

    fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is FirebaseAuthWeakPasswordException -> RegisterException("Weak password")
            is FirebaseAuthInvalidCredentialsException -> RegisterException("Invalid email")
            is FirebaseAuthUserCollisionException -> RegisterException("The email is already taken")
            else -> RegisterException("Failed to register, try again")
        }
    }
}