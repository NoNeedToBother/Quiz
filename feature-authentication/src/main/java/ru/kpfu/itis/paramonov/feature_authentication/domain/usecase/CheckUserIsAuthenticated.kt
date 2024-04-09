package ru.kpfu.itis.paramonov.feature_authentication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.feature_authentication.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class CheckUserIsAuthenticated @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
) {
    suspend fun invoke(): Boolean {
        return repository.checkUserIsAuthenticated()
    }
}