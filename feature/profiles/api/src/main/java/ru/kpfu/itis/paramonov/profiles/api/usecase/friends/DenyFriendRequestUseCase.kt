package ru.kpfu.itis.paramonov.profiles.api.usecase.friends

interface DenyFriendRequestUseCase {

    suspend operator fun invoke(id: String)

}
