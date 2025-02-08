package ru.kpfu.itis.paramonov.authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase

class CheckUserIsAuthenticatedUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository
): CheckUserIsAuthenticatedUseCase {
    override suspend operator fun invoke(): User? {
        return withContext(dispatcher) {
            repository.checkUserIsAuthenticated()
        }
    }
}
