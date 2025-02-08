package ru.kpfu.itis.paramonov.questions.api.model

data class User(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val info: String,
    val dateRegistered: String,
    val friendIdList: List<String>,
    val friendRequestFromList: List<String>
)
