package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository,
    private val mapper: UserUiModelMapper
) {
    suspend operator fun invoke(username: String, password: String): UserModel {
        return withContext(dispatcher) {
            val firebaseUser = repository.authenticateUser(username, password)
            mapper.map(firebaseUser)
        }
    }
}