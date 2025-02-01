package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase
import javax.inject.Inject

class LogoutUserUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): LogoutUserUseCase {

    override suspend operator fun invoke(onLogout: () -> Unit) {
        withContext(dispatcher) {
            repository.logoutUser(onLogout)
        }
    }
}