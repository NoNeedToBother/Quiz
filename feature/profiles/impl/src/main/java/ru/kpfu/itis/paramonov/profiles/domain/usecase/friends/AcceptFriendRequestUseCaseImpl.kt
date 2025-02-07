package ru.kpfu.itis.paramonov.profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.AcceptFriendRequestUseCase

class AcceptFriendRequestUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val friendRepository: FriendRepository
): AcceptFriendRequestUseCase {

    override suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            friendRepository.acceptFriendRequest(id)
        }
    }
}