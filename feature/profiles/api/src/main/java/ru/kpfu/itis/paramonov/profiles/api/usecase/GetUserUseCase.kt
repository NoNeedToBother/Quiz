package ru.kpfu.itis.paramonov.profiles.api.usecase

import ru.kpfu.itis.paramonov.profiles.api.model.User

interface GetUserUseCase {

    suspend operator fun invoke(id: String): User

}
