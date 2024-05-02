package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository
import java.util.Optional
import javax.inject.Inject

class CheckUserIsAuthenticatedUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository,
    private val mapper: UserModelMapper
) {
    suspend fun invoke(): Optional<UserModel> {
        return withContext(dispatcher) {
            val user = repository.checkUserIsAuthenticated()
            if (user.isPresent) {
                Optional.of(mapper.map(user.get()))
            } else {
                Optional.empty()
            }
        }
    }
}