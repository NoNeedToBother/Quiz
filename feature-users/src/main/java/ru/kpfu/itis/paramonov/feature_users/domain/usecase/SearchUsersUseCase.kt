package ru.kpfu.itis.paramonov.feature_users.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val userUiModelMapper: UserUiModelMapper
) {

    suspend operator fun invoke(username: String): List<UserModel> {
        return withContext(dispatcher) {
            userRepository.findByUsername(username)
                .map {
                    user -> userUiModelMapper.map(user)
                }
        }
    }
}