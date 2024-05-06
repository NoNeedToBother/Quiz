package ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository
import javax.inject.Inject

class GetDifficultyUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val settingsRepository: QuestionSettingsRepository,
    private val mapper: QuestionSettingsUiModelMapper
){

    suspend operator fun invoke(): DifficultyUiModel {
        return withContext(dispatcher) {
            mapper.mapDifficulty(settingsRepository.getDifficulty())
        }
    }
}