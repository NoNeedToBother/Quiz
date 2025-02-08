package ru.kpfu.itis.paramonov.profiles.domain.usecase.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.ChangeCredentialsUseCase

class ChangeCredentialsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): ChangeCredentialsUseCase {

    override suspend operator fun invoke(email: String?, password: String?) {
        withContext(dispatcher) {
            repository.updateCredentials(email, password)
        }
    }
}
