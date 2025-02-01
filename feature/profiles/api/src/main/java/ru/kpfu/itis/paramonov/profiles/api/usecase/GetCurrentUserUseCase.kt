package ru.kpfu.itis.paramonov.profiles.api.usecase

import ru.kpfu.itis.paramonov.profiles.api.model.User

interface GetCurrentUserUseCase {

    suspend fun invoke(): User

}