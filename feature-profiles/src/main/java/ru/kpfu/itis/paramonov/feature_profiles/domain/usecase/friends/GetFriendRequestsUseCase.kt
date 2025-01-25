package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_profiles.R
import ru.kpfu.itis.paramonov.feature_profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.exceptions.UserDataException
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
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