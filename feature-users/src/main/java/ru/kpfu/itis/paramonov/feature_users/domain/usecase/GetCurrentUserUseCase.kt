package ru.kpfu.itis.paramonov.feature_users.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_users.domain.mapper.UserModelMapper
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val mapper: UserModelMapper,
    private val resourceManager: ResourceManager
) {
    suspend fun invoke(): UserModel {
        return withContext(dispatcher) {
            try {
                val user = repository.getCurrentUser()
                if (user.isPresent) {
                    mapper.map(user.get())
                } else throw IncorrectUserDataException(
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