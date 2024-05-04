package ru.kpfu.itis.paramonov.feature_users.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.data.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class GetFriendRequestsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val userUiModelMapper: UserUiModelMapper,
    private val resourceManager: ResourceManager
){

    suspend operator fun invoke(): List<UserModel> {
        val users = mutableListOf<UserModel>()
        return withContext(dispatcher) {
            userRepository.getCurrentUser()?.let {
                it.friendRequestFromList.forEach { id ->
                    userRepository.getUser(id)?.let { user ->
                        users.add(userUiModelMapper.map(user))
                    }
                }
                users
            } ?: throw UserDataException(
                resourceManager.getString(R.string.get_req_fail)
            )
        }
    }
}