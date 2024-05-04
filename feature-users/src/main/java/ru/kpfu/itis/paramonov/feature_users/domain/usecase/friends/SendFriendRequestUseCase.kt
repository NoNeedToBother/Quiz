package ru.kpfu.itis.paramonov.feature_users.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.domain.repository.FriendRepository
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            friendRepository.sendFriendRequest(id)
        }
    }
}