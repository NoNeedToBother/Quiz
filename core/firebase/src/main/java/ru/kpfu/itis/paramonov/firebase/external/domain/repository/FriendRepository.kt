package ru.kpfu.itis.paramonov.firebase.external.domain.repository

interface FriendRepository {

    suspend fun sendFriendRequest(id: String)

    suspend fun acceptFriendRequest(id: String)

    suspend fun denyFriendRequest(id: String)

}