package ru.kpfu.itis.paramonov.leaderboards.presentation.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.utils.toEnumName
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetDifficultyUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsDomainModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.GameModeUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.SettingUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenSideEffect
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenState

class LeaderboardsViewModel(
    private val getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
    private val getFriendsLeaderboardUseCase: GetFriendsLeaderboardUseCase,
    private val getGameModeUseCase: GetGameModeUseCase,
    private val getDifficultyUseCase: GetDifficultyUseCase,
    private val questionSettingsDomainModelMapper: QuestionSettingsDomainModelMapper,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper
): ViewModel(), ContainerHost<LeaderboardsScreenState, LeaderboardsScreenSideEffect> {

    override val container = container<LeaderboardsScreenState, LeaderboardsScreenSideEffect>(LeaderboardsScreenState())

    fun saveSettings() = intent {
        val settings = SettingUiModel(
            difficulty = state.settings?.difficulty,
            category = state.settings?.category,
            gameMode = state.settings?.gameMode
                ?: questionSettingsUiModelMapper.mapGameMode(getGameModeUseCase.invoke())
        )
        reduce { state.copy(settings = settings, results = emptyList(), loadingEnded = false) }
    }

    private fun checkLeaderboard(results: List<ResultUiModel>): Boolean {
        if (results.isEmpty()) return true
        val currentSettings = container.stateFlow.value.settings
        val result = results[0]
        currentSettings?.run {
            if (gameMode != result.gameMode) return false
            category?.let {
                if (it != result.category) return false
            }
            difficulty?.let {
                if (it != result.difficulty) return false
            }
        }
        return true
    }

    fun getGlobalLeaderboard(max: Int) = intent {
        if (state.settings == null) sendInitialSettingData().join()
        reduce { state.copy(results = emptyList(), loadingEnded = false) }
        try {
            val settings = state.settings
            val leaderboard = getGlobalLeaderboardUseCase.invoke(
                gameMode = settings?.let {
                    questionSettingsDomainModelMapper.mapGameMode(it.gameMode)
                } ?: getGameModeUseCase.invoke(),
                difficulty = settings?.difficulty?.let {
                    questionSettingsDomainModelMapper.mapDifficulty(it)
                } ?: getDifficultyUseCase.invoke(),
                category = settings?.category?.let {
                    questionSettingsDomainModelMapper.mapCategory(it)
                },
                max = max, afterScore = null
            ).map { model -> resultUiModelMapper.map(model) }
            if (checkLeaderboard(leaderboard))
                reduce { state.copy(results = leaderboard, loadingEnded = true) }
        } catch (ex: Throwable) {
            postSideEffect(LeaderboardsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun getFriendsLeaderboard(max: Int) = intent {
        if (state.settings == null) sendInitialSettingData().join()
        reduce { state.copy(results = emptyList(), loadingEnded = false) }
        try {
            val settings = state.settings
            val leaderboard = getFriendsLeaderboardUseCase.invoke(
                gameMode = settings?.let {
                    questionSettingsDomainModelMapper.mapGameMode(it.gameMode)
                } ?: getGameModeUseCase.invoke(),
                difficulty = settings?.difficulty?.let {
                    questionSettingsDomainModelMapper.mapDifficulty(it)
                } ?: getDifficultyUseCase.invoke(),
                category = settings?.category?.let {
                    questionSettingsDomainModelMapper.mapCategory(it)
                },
                max = max, afterScore = null
            ).map { model -> resultUiModelMapper.map(model) }
            if (checkLeaderboard(leaderboard))
                reduce { state.copy(results = leaderboard, loadingEnded = true) }
        } catch (ex: Throwable) {
            postSideEffect(LeaderboardsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    private fun sendInitialSettingData() = intent {
        val settings = SettingUiModel(
            gameMode = questionSettingsUiModelMapper.mapGameMode(getGameModeUseCase.invoke()),
            difficulty = questionSettingsUiModelMapper.mapDifficulty(getDifficultyUseCase.invoke()),
            category = null
        )

        reduce { state.copy(settings = settings) }
    }

    fun loadNextFriendsResults(max: Int, startAfter: Double) = intent {
        try {
            val settings = state.settings
            val leaderboard = getFriendsLeaderboardUseCase.invoke(
                gameMode = settings?.let {
                    questionSettingsDomainModelMapper.mapGameMode(it.gameMode)
                } ?: getGameModeUseCase.invoke(),
                difficulty = settings?.difficulty?.let {
                    questionSettingsDomainModelMapper.mapDifficulty(it)
                },
                category = settings?.category?.let {
                    questionSettingsDomainModelMapper.mapCategory(it)
                },
                max = max, afterScore = startAfter
            ).map { model -> resultUiModelMapper.map(model) }
            if (checkLeaderboard(leaderboard)) {
                val new = ArrayList(state.results)
                new.addAll(leaderboard)
                reduce { state.copy(results = new.distinctBy { it.id }) }
            }
        } catch (ex: Throwable) {
            postSideEffect(LeaderboardsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun loadNextGlobalResults(max: Int, startAfter: Double) = intent {
        try {
            val settings = state.settings
            val leaderboard = getGlobalLeaderboardUseCase.invoke(
                gameMode = settings?.let {
                    questionSettingsDomainModelMapper.mapGameMode(it.gameMode)
                } ?: getGameModeUseCase.invoke(),
                difficulty = settings?.difficulty?.let {
                    questionSettingsDomainModelMapper.mapDifficulty(it)
                },
                category = settings?.category?.let {
                    questionSettingsDomainModelMapper.mapCategory(it)
                },
                max = max, afterScore = startAfter
            ).map { model -> resultUiModelMapper.map(model) }
            if (checkLeaderboard(leaderboard)) {
                val new = ArrayList(state.results)
                new.addAll(leaderboard)
                reduce { state.copy(results = new.distinctBy { it.id }) }
            }
        } catch (ex: Throwable) {
            postSideEffect(LeaderboardsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun changeDifficulty(difficulty: String) = intent {
        val gameMode = state.settings?.gameMode ?: questionSettingsUiModelMapper.mapGameMode(
            getGameModeUseCase.invoke()
        )
        if (difficulty == "Any")
            reduce { state.copy(settings = SettingUiModel(
                difficulty = null,
                category = state.settings?.category,
                gameMode = gameMode))
            }
        else {
            reduce { state.copy(settings = SettingUiModel(
                difficulty = DifficultyUiModel.valueOf(difficulty.toEnumName()),
                category = state.settings?.category,
                gameMode = gameMode))
            }
        }
    }

    fun changeCategory(category: String) = intent {
        val gameMode = state.settings?.gameMode ?: questionSettingsUiModelMapper.mapGameMode(
            getGameModeUseCase.invoke()
        )
        if (category == "Any")
            reduce { state.copy(settings = SettingUiModel(
                difficulty = state.settings?.difficulty,
                category = null,
                gameMode = gameMode))
            }
        else {
            reduce { state.copy(settings = SettingUiModel(
                difficulty = state.settings?.difficulty,
                category = CategoryUiModel.valueOf(category.toEnumName()),
                gameMode = gameMode))
            }
        }
    }

    fun changeGameMode(gameMode: String) = intent {
        reduce { state.copy(settings = SettingUiModel(
            difficulty = state.settings?.difficulty,
            category = state.settings?.category,
            gameMode = GameModeUiModel.valueOf(gameMode.toEnumName())))
        }
    }
}
