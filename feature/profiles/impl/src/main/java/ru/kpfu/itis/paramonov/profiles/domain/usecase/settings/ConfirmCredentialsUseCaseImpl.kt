package ru.kpfu.itis.paramonov.profiles.domain.usecase.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.ConfirmCredentialsUseCase

class ConfirmCredentialsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): ConfirmCredentialsUseCase {

    override suspend operator fun invoke(email: String, password: String) {
        withContext(dispatcher) {
            repository.reauthenticate(email = email, password = password)
        }
    }
}
