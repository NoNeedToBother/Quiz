package ru.kpfu.itis.paramonov.profiles.api.usecase.friends

import ru.kpfu.itis.paramonov.profiles.api.model.User

interface GetFriendRequestsUseCase {

    suspend operator fun invoke(): List<User>

}