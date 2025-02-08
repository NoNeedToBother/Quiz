package ru.kpfu.itis.paramonov.profiles.api.usecase.friends

interface SendFriendRequestUseCase {

    suspend operator fun invoke(id: String)

}
