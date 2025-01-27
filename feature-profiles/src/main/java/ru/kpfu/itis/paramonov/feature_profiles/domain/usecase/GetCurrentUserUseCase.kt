package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_profiles.R
import ru.kpfu.itis.paramonov.feature_profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val mapper: UserUiModelMapper,
    private val resourceManager: ResourceManager
) {
    suspend fun invoke(): UserModel {
        return withContext(dispatcher) {
            try {
                val user = repository.getCurrentUser()
                user?.let {
                    mapper.map(it)
                } ?: throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_user_data)
                )
            } catch (ex: Throwable) {
                throw IncorrectUserDataException(
                    resourceManager.getString(R.string.incorrect_user_data)
                )
            }
        }
    }
}