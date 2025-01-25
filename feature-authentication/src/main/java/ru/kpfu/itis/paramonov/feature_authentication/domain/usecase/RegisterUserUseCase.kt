package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository,
    private val mapper: UserUiModelMapper
) {
    suspend operator fun invoke(username: String, email: String,
                                password: String, confirmPassword: String): UserModel {
        return withContext(dispatcher) {
            val firebaseUser = repository.registerUser(username, email, password, confirmPassword)
            mapper.map(firebaseUser)
        }
    }
}