package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel

import android.widget.ImageView
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.common_android.utils.emitException
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetDifficultyUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardFragment
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.SettingUiModel
import ru.kpfu.itis.paramonov.navigation.UserRouter

class LeaderboardsViewModel(
    private val getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
    private val getFriendsLeaderboardUseCase: GetFriendsLeaderboardUseCase,
    private val getGameModeUseCase: GetGameModeUseCase,
    private val getDifficultyUseCase: GetDifficultyUseCase,
    private val userRouter: UserRouter
): BaseViewModel() {

    private val _globalLeaderboardDataFlow = MutableStateFlow<LeaderboardDataResult?>(null)

    private val globalLeaderboardDataFlow: StateFlow<LeaderboardDataResult?> get() = _globalLeaderboardDataFlow

    private val _friendsLeaderboardDataFlow = MutableStateFlow<LeaderboardDataResult?>(null)

    private val friendsLeaderboardDataFlow: StateFlow<LeaderboardDataResult?> get() = _friendsLeaderboardDataFlow

    private val _settingsDataFlow = MutableStateFlow<SettingUiModel?>(null)

    val settingsDataFlow: StateFlow<SettingUiModel?> get() = _settingsDataFlow

    private val _clearLeaderboardFlow = MutableStateFlow(false)

    val clearLeaderboardFlow: StateFlow<Boolean> get() = _clearLeaderboardFlow

    fun navigateToUser(id: String, sharedView: ImageView) {
        viewModelScope.launch {
            userRouter.withSharedView(sharedView) {
                goToUser(id)
            }
        }
    }

    fun clearLeaderboards() {
        viewModelScope.launch {
            _clearLeaderboardFlow.value = true
        }
    }

    fun onLeaderboardCleared() {
        viewModelScope.launch {
            _clearLeaderboardFlow.value = false
        }
    }

    fun saveSettings(category: CategoryUiModel?, difficulty: DifficultyUiModel?, gameMode: GameModeUiModel) {
        viewModelScope.launch {
            _settingsDataFlow.value = SettingUiModel(
                difficulty = difficulty, category = category, gameMode = gameMode
            )
        }
    }

    fun getResultsOnStart(type: LeaderboardFragment.LeaderboardType, max: Int) {
        when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> getGlobalLeaderboardOnStart(max)
            LeaderboardFragment.LeaderboardType.FRIENDS -> getFriendsLeaderboardOnStart(max)
        }
    }

    fun getResultsAfterCleared(type: LeaderboardFragment.LeaderboardType, max: Int) {
        when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> getGlobalLeaderboardAfterCleared(max)
            LeaderboardFragment.LeaderboardType.FRIENDS -> getFriendsLeaderboardAfterCleared(max)
        }
    }

    private fun checkLeaderboard(results: List<ResultUiModel>): Boolean {
        if (results.isEmpty()) return true
        val currentSettings = _settingsDataFlow.value
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
    private fun getFriendsLeaderboardAfterCleared(max: Int) {
        viewModelScope.launch {
            try {
                val leaderboard = getFriendsLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty,
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = null
                )
                if (checkLeaderboard(leaderboard))
                    _friendsLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _friendsLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    private fun getGlobalLeaderboardAfterCleared(max: Int) {
        viewModelScope.launch {
            try {
                val leaderboard = getGlobalLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty,
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = null
                )
                if (checkLeaderboard(leaderboard))
                    _globalLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _globalLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    private fun getGlobalLeaderboardOnStart(max: Int) {
        viewModelScope.launch {
            sendInitialSettingData()
            try {
                val leaderboard = getGlobalLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty ?: getDifficultyUseCase.invoke(),
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = null
                )
                if (checkLeaderboard(leaderboard))
                    _globalLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _globalLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    private fun getFriendsLeaderboardOnStart(max: Int) {
        viewModelScope.launch {
            try {
                val leaderboard = getFriendsLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty ?: getDifficultyUseCase.invoke(),
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = null
                )
                if (checkLeaderboard(leaderboard))
                    _friendsLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _friendsLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    private suspend fun sendInitialSettingData() {
        _settingsDataFlow.value = SettingUiModel(
            gameMode = getGameModeUseCase.invoke(),
            difficulty = getDifficultyUseCase.invoke(),
            category = null
        )
    }

    fun getDataFlow(type: LeaderboardFragment.LeaderboardType): StateFlow<LeaderboardDataResult?> {
        return when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> globalLeaderboardDataFlow
            LeaderboardFragment.LeaderboardType.FRIENDS -> friendsLeaderboardDataFlow
        }
    }

    fun loadNextResults(type: LeaderboardFragment.LeaderboardType, max: Int, startAfter: Double) {
        when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> loadNextGlobalResults(max, startAfter)
            LeaderboardFragment.LeaderboardType.FRIENDS -> loadNextFriendsResults(max, startAfter)
        }
    }

    private fun loadNextFriendsResults(max: Int, startAfter: Double) {
        viewModelScope.launch {
            try {
                val leaderboard = getFriendsLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty,
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = startAfter
                )
                if (checkLeaderboard(leaderboard))
                    _friendsLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _friendsLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    private fun loadNextGlobalResults(max: Int, startAfter: Double) {
        viewModelScope.launch {
            try {
                val leaderboard = getGlobalLeaderboardUseCase.invoke(
                    gameModeUiModel = _settingsDataFlow.value?.gameMode ?: getGameModeUseCase.invoke(),
                    difficultyUiModel = _settingsDataFlow.value?.difficulty,
                    categoryUiModel = _settingsDataFlow.value?.category,
                    max = max, afterScore = startAfter
                )
                if (checkLeaderboard(leaderboard))
                    _globalLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _globalLeaderboardDataFlow.emitException(LeaderboardDataResult.Failure(ex))
            }
        }
    }

    sealed interface LeaderboardDataResult: Result {
        class Success(private val result: List<ResultUiModel>): LeaderboardDataResult,
            Result.Success<List<ResultUiModel>> {
            override fun getValue(): List<ResultUiModel> = result
        }

        class Failure(private val ex: Throwable): LeaderboardDataResult, Result.Failure {
            override fun getException(): Throwable = ex

        }
    }
}