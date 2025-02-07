package ru.kpfu.itis.paramonov.users.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.users.api.model.User
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository
import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase

class GetFriendsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
): GetFriendsUseCase {

    override suspend operator fun invoke(offset: Int, max: Int): List<User> {
        return withContext(dispatcher) {
            userRepository.getFriends(offset, max)
        }
    }
}