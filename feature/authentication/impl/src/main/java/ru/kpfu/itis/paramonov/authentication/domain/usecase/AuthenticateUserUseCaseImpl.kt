package ru.kpfu.itis.paramonov.authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.authentication.api.usecase.AuthenticateUserUseCase

class AuthenticateUserUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository
): AuthenticateUserUseCase {
    override suspend operator fun invoke(username: String, password: String): User {
        return withContext(dispatcher) {
            repository.authenticateUser(username, password)
        }
    }
}
