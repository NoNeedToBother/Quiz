package ru.kpfu.itis.paramonov.common.model.presentation

data class UserModel(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val dateRegistered: String,
    val info: String
)
