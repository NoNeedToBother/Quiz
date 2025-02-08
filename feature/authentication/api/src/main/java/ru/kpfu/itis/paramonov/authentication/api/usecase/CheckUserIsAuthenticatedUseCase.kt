package ru.kpfu.itis.paramonov.authentication.api.usecase

import ru.kpfu.itis.paramonov.authentication.api.model.User

interface CheckUserIsAuthenticatedUseCase {

    suspend operator fun invoke(): User?
}
