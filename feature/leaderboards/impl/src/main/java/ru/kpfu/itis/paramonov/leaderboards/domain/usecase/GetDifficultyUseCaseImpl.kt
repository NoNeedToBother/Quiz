package ru.kpfu.itis.paramonov.leaderboards.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetDifficultyUseCase
import javax.inject.Inject

class GetDifficultyUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val settingsRepository: QuestionSettingsRepository
): GetDifficultyUseCase {

    override suspend operator fun invoke(): Difficulty {
        return withContext(dispatcher) {
            settingsRepository.getDifficulty()
        }
    }
}