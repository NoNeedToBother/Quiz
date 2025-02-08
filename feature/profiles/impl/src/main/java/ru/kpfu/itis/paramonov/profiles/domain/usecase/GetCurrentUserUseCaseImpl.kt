package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserUseCase

class GetCurrentUserUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val resourceManager: ResourceManager
): GetCurrentUserUseCase {
    override suspend fun invoke(): User {
        return withContext(dispatcher) {
            try {
                val user = repository.getCurrentUser()
                user ?: throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_user_data)
                )
            } catch (ex: Throwable) {
                throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_user_data)
                )
            }
        }
    }
}
