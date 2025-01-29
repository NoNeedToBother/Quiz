package ru.kpfu.itis.paramonov.authentication.api.usecase

import ru.kpfu.itis.paramonov.authentication.api.model.User

interface AuthenticateUserUseCase {

    suspend operator fun invoke(username: String, password: String): User

}