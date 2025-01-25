package ru.kpfu.itis.paramonov.feature_users.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val userUiModelMapper: UserUiModelMapper
) {

    suspend operator fun invoke(offset: Int, max: Int): List<UserModel> {
        return withContext(dispatcher) {
            userRepository.getFriends(offset, max)
                .map {
                        user -> userUiModelMapper.map(user)
                }
        }
    }
}