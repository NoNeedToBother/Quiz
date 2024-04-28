package ru.kpfu.itis.paramonov.common.model

data class UserModel(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val info: String
)
