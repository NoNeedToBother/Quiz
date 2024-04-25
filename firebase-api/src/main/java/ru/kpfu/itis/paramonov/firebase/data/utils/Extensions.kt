package ru.kpfu.itis.paramonov.firebase.data.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.delay
import ru.kpfu.itis.paramonov.firebase.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser

suspend fun <T> Task<T>.waitResult(): Task<T> {
    while (true) {
        if (isComplete) break
        else delay(100L)
    }
    return this
}

fun DataSnapshot.getUser(): FirebaseUser {
    val id = child(UserRepositoryImpl.DB_ID_FIELD).value as String
    val username = child(UserRepositoryImpl.DB_USERNAME_FIELD).value as String
    return FirebaseUser(
        id, username
    )
}