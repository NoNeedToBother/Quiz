package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import java.util.Optional
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val mapper: UserModelMapper
) {
    suspend fun invoke(): Optional<UserModel> {
        return withContext(dispatcher) {
            val user = repository.getCurrentUser()
            if (user.isPresent) {
                Optional.of(mapper.map(user.get()))
            } else {
                Optional.empty()
            }
        }
    }
}