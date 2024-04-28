package ru.kpfu.itis.paramonov.firebase.domain.model

data class FirebaseUser(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val info: String
) {
}