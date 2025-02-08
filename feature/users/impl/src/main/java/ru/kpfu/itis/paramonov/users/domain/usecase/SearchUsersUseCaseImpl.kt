package ru.kpfu.itis.paramonov.users.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.users.api.model.User
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository
import ru.kpfu.itis.paramonov.users.api.usecase.SearchUsersUseCase

class SearchUsersUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
): SearchUsersUseCase {

    override suspend operator fun invoke(username: String, max: Int, lastId: String?): List<User> {
        return withContext(dispatcher) {
            userRepository.findByUsername(username, max, lastId)
        }
    }
}
