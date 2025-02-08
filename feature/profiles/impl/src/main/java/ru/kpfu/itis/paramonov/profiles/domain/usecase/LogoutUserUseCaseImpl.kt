package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase

class LogoutUserUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): LogoutUserUseCase {

    override suspend operator fun invoke(onLogout: suspend () -> Unit) {
        withContext(dispatcher) {
            repository.logoutUser(onLogout)
        }
    }
}
