package ru.kpfu.itis.paramonov.questions.api.repository

import ru.kpfu.itis.paramonov.questions.api.model.User

interface UserRepository {

    suspend fun getCurrentUser(): User?

}