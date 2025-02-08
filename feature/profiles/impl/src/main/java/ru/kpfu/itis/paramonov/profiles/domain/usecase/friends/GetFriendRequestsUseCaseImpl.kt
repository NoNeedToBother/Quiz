package ru.kpfu.itis.paramonov.profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException

class GetFriendRequestsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val resourceManager: ResourceManager
): GetFriendRequestsUseCase {

    override suspend operator fun invoke(): List<User> {
        val users = mutableListOf<User>()
        return withContext(dispatcher) {
            userRepository.getCurrentUser()?.let {
                it.friendRequestFromList.forEach { id ->
                    userRepository.getUser(id)?.let { user ->
                        users.add(user)
                    }
                }
                users
            } ?: throw IncorrectUserDataException(
                resourceManager.getString(R.string.get_req_fail)
            )
        }
    }
}
