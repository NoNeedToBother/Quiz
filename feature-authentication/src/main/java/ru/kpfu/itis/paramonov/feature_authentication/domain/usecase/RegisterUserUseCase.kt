package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository,
    private val mapper: UserModelMapper
) {
    suspend operator fun invoke(username: String, email: String,
                                password: String, confirmPassword: String): UserModel {
        return withContext(dispatcher) {
            val firebaseUser = repository.registerUser(username, email, password, confirmPassword)
            mapper.map(firebaseUser)
        }
    }
}