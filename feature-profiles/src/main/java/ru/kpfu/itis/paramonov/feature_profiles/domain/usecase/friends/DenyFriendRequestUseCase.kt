package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.FriendRepository
import javax.inject.Inject

class DenyFriendRequestUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val friendRepository: FriendRepository
) {

    suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            friendRepository.denyFriendRequest(id)
        }
    }
}