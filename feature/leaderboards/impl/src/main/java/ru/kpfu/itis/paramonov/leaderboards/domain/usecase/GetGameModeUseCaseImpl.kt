package ru.kpfu.itis.paramonov.leaderboards.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGameModeUseCase
import javax.inject.Inject

class GetGameModeUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val settingsRepository: QuestionSettingsRepository
): GetGameModeUseCase {

    override suspend operator fun invoke(): GameMode {
        return withContext(dispatcher) {
            settingsRepository.getGameMode()
        }
    }
}