package ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ChangeCredentialsUseCase
import javax.inject.Inject

class ChangeCredentialsUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): ChangeCredentialsUseCase {

    override suspend operator fun invoke(email: String?, password: String?) {
        withContext(dispatcher) {
            repository.updateCredentials(email, password)
        }
    }
}