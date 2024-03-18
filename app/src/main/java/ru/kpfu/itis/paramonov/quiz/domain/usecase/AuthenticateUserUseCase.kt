package ru.kpfu.itis.paramonov.quiz.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.quiz.presentation.model.UserModel
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val mapper: UserModelMapper
) {
    suspend operator fun invoke(username: String, password: String): UserModel {
        return withContext(dispatcher) {
            val firebaseUser = repository.authenticateUser(username, password)
            mapper.fromFirebaseDomainModel(firebaseUser)
        }
    }
}