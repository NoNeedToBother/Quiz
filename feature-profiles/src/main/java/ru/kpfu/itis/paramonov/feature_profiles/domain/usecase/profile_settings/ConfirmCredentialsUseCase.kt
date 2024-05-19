package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class ConfirmCredentialsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
){

    suspend operator fun invoke(email: String, password: String) {
        withContext(dispatcher) {
            repository.reauthenticate(email = email, password = password)
        }
    }
}