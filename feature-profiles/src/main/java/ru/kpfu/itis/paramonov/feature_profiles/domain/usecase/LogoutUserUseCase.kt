package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
) {

    suspend operator fun invoke(onLogout: () -> Unit) {
        withContext(dispatcher) {
            repository.logoutUser(onLogout)
        }
    }
}