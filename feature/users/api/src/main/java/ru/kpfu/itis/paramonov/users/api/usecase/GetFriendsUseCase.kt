package ru.kpfu.itis.paramonov.users.api.usecase

import ru.kpfu.itis.paramonov.users.api.model.User

interface GetFriendsUseCase {

    suspend operator fun invoke(offset: Int, max: Int): List<User>

}
