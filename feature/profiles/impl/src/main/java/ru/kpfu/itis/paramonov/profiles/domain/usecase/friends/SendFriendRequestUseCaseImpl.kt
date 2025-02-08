package ru.kpfu.itis.paramonov.profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.SendFriendRequestUseCase

class SendFriendRequestUseCaseImpl(
    private val friendRepository: FriendRepository,
    private val dispatcher: CoroutineDispatcher
): SendFriendRequestUseCase {

    override suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            friendRepository.sendFriendRequest(id)
        }
    }
}
