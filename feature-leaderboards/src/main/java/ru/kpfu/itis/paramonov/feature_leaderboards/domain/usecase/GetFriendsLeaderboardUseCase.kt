package ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_leaderboards.data.datasource.ResultSettingsMediator
import ru.kpfu.itis.paramonov.feature_leaderboards.data.datasource.ResultSource
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FriendsResultSource
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import javax.inject.Inject

class GetFriendsLeaderboardUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    @FriendsResultSource private val dataSource: ResultSource,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val questionSettingsApiModelMapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(
        gameModeUiModel: GameModeUiModel,
        difficultyUiModel: DifficultyUiModel?,
        categoryUiModel: CategoryUiModel?,
        afterScore: Double?,
        scope: CoroutineScope
    ): StateFlow<PagingData<ResultUiModel>> {
        return withContext(dispatcher) {
            val mediator = ResultSettingsMediator(
                gameMode = questionSettingsApiModelMapper.mapGameMode(gameModeUiModel),
                difficulty = difficultyUiModel?.let {
                    questionSettingsApiModelMapper.mapDifficulty(it)
                },
                category = categoryUiModel?.let {
                    questionSettingsApiModelMapper.mapCategory(it)
                },
                after = afterScore
            )
            Pager(
                PagingConfig(prefetchDistance = 2, enablePlaceholders = false, pageSize = 5, maxSize = 25)
            ) {
                dataSource.withMediator(mediator)
            }.flow.map {
                it.map { result ->
                    resultUiModelMapper.map(result)
                }
            }
                .cachedIn(scope)
                .stateIn(scope, SharingStarted.Lazily, PagingData.empty())
        }
    }
}