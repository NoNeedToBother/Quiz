package ru.kpfu.itis.paramonov.feature_users.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class DenyFriendRequestUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(id: String) {
        withContext(dispatcher) {
            userRepository.denyFriendRequest(id)
        }
    }
}