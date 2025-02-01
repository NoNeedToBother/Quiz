package ru.kpfu.itis.paramonov.users.api.usecase

import ru.kpfu.itis.paramonov.users.api.model.User

interface SearchUsersUseCase {

    suspend operator fun invoke(username: String, max: Int, lastId: String?): List<User>
    
}