package ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import javax.inject.Inject

class GetGlobalLeaderboardUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val resultRepository: ResultRepository,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val questionSettingsApiModelMapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(
        gameModeUiModel: GameModeUiModel,
        difficultyUiModel: DifficultyUiModel?,
        categoryUiModel: CategoryUiModel?,
        max: Int, afterScore: Double?
    ): List<ResultUiModel> {
        return withContext(dispatcher) {
            resultRepository.get(
                gameMode = questionSettingsApiModelMapper.mapGameMode(gameModeUiModel),
                difficulty = difficultyUiModel?.let {
                    questionSettingsApiModelMapper.mapDifficulty(it)
                },
                category = categoryUiModel?.let {
                    questionSettingsApiModelMapper.mapCategory(it)
                },
                max = max, afterScore = afterScore
            ).map {
                resultUiModelMapper.map(it)
            }
        }
    }
}