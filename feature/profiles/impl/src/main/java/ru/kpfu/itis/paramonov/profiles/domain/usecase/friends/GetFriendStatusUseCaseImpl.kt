package ru.kpfu.itis.paramonov.profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.api.model.FriendStatus
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import javax.inject.Inject

class GetFriendStatusUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager
): GetFriendStatusUseCase {

    override suspend operator fun invoke(id: String): FriendStatus {
        return withContext(dispatcher) {
            userRepository.getCurrentUser()?.let { from ->
                userRepository.getUser(id)?.let {  to ->
                    if (to.id == from.id) FriendStatus.SAME_USER
                    else if (to.friendIdList.contains(from.id)) FriendStatus.FRIEND
                    else if (to.friendRequestFromList.contains(from.id)) FriendStatus.REQUEST_SENT
                    else FriendStatus.NOT_FRIEND
                } ?: throw IncorrectUserDataException(resourceManager.getString(R.string.incorrect_other_user_data))
            } ?: throw IncorrectUserDataException(resourceManager.getString(R.string.incorrect_other_user_data))
        }
    }
}