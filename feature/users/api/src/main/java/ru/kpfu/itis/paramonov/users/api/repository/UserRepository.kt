package ru.kpfu.itis.paramonov.users.api.repository

import ru.kpfu.itis.paramonov.users.api.model.User

interface UserRepository {

    suspend fun findByUsername(username: String, max: Int, lastId: String?): List<User>

    suspend fun getFriends(offset: Int, max: Int): List<User>

}