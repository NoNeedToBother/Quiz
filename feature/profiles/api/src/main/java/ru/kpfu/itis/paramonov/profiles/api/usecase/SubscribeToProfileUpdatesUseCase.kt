package ru.kpfu.itis.paramonov.profiles.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.paramonov.profiles.api.model.User

interface SubscribeToProfileUpdatesUseCase {

    suspend operator fun invoke(): Flow<User>

}