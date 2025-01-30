package ru.kpfu.itis.paramonov.profiles.api.usecase.friends

import ru.kpfu.itis.paramonov.profiles.api.model.FriendStatus

interface GetFriendStatusUseCase {

    suspend operator fun invoke(id: String): FriendStatus

}