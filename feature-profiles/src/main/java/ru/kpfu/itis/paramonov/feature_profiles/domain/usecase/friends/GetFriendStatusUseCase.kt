package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_profiles.R
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.FriendStatusUiModel
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class GetFriendStatusUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager
) {

    suspend operator fun invoke(id: String): FriendStatusUiModel{
        return withContext(dispatcher) {
            userRepository.getCurrentUser()?.let { from ->
                userRepository.getUser(id)?.let {  to ->
                    if (to.id == from.id) FriendStatusUiModel.SAME_USER
                    else if (to.friendIdList.contains(from.id)) FriendStatusUiModel.FRIEND
                    else if (to.friendRequestFromList.contains(from.id)) FriendStatusUiModel.REQUEST_SENT
                    else FriendStatusUiModel.NOT_FRIEND
                } ?: throw UserDataException(resourceManager.getString(R.string.incorrect_other_user_data))
            } ?: throw UserDataException(resourceManager.getString(R.string.incorrect_other_user_data))
        }
    }
}