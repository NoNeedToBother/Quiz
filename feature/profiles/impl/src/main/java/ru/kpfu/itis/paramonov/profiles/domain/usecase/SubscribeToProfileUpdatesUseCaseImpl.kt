package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.SubscribeToProfileUpdatesUseCase

class SubscribeToProfileUpdatesUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
): SubscribeToProfileUpdatesUseCase {

    override suspend operator fun invoke(): Flow<User> {
        return withContext(dispatcher) {
            userRepository.subscribeToProfileUpdates()
        }
    }
}
