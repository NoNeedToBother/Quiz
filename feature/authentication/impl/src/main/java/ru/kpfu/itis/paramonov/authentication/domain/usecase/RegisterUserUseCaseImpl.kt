package ru.kpfu.itis.paramonov.authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.authentication.api.usecase.RegisterUserUseCase

class RegisterUserUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AuthenticationRepository
): RegisterUserUseCase {
    override suspend operator fun invoke(username: String, email: String,
                                         password: String, confirmPassword: String): User {
        return withContext(dispatcher) {
            repository.registerUser(username, email, password, confirmPassword)
        }
    }
}
