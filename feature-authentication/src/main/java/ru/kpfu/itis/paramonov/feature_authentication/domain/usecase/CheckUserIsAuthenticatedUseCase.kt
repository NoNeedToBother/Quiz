package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository
import javax.inject.Inject

class CheckUserIsAuthenticatedUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository,
    private val mapper: UserUiModelMapper
) {
    suspend fun invoke(): UserModel? {
        return withContext(dispatcher) {
            val user = repository.checkUserIsAuthenticated()
            user?.let {
                mapper.map(it)
            }
        }
    }
}