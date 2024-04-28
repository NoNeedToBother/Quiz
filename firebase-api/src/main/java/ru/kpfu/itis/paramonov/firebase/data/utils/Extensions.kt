package ru.kpfu.itis.paramonov.firebase.data.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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

fun DocumentSnapshot.getUser(): FirebaseUser {
    val id = data?.get(UserRepositoryImpl.DB_ID_FIELD) as String
    val username = data?.get(UserRepositoryImpl.DB_USERNAME_FIELD) as String
    val profilePicture = data?.get(UserRepositoryImpl.DB_PROFILE_PICTURE_FIELD) as String
    val info = data?.get(UserRepositoryImpl.DB_INFO_FIELD) as String
    return FirebaseUser(
        id, username, profilePicture, info
    )
}