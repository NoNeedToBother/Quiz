package ru.kpfu.itis.paramonov.profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.DenyFriendRequestUseCase
import javax.inject.Inject

class DenyFriendRequestUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val friendRepository: FriendRepository
): DenyFriendRequestUseCase {

    override suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            friendRepository.denyFriendRequest(id)
        }
    }
}