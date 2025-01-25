package ru.kpfu.itis.paramonov.core.model.presentation

data class UserModel(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val dateRegistered: String,
    val info: String,
    val friendIdList: List<String>,
    val friendRequestFromList: List<String>
)
