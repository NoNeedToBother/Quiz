package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserUseCase

class GetUserUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val resourceManager: ResourceManager
): GetUserUseCase {

    override suspend operator fun invoke(id: String): User {
        return withContext(dispatcher) {
            try {
                val user = repository.getUser(id)
                user ?: throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_other_user_data)
                )
            } catch (ex: Throwable) {
                throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_other_user_data)
                )
            }
        }
    }
}