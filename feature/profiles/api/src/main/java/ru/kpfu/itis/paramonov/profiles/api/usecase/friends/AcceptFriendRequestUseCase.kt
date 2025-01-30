package ru.kpfu.itis.paramonov.profiles.api.usecase.friends

interface AcceptFriendRequestUseCase {

    suspend operator fun invoke(id: String)

}