package ru.kpfu.itis.paramonov.authentication.api.usecase

import ru.kpfu.itis.paramonov.authentication.api.model.User

interface RegisterUserUseCase {
    suspend operator fun invoke(username: String, email: String,
                                password: String, confirmPassword: String): User
}