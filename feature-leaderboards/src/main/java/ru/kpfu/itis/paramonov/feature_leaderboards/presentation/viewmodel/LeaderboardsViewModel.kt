package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardFragment
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.navigation.UserRouter

class LeaderboardsViewModel(
    private val getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
    private val getFriendsLeaderboardUseCase: GetFriendsLeaderboardUseCase,
    private val getGameModeUseCase: GetGameModeUseCase,
    private val userRouter: UserRouter
): BaseViewModel() {

    private val _globalLeaderboardDataFlow = MutableStateFlow<LeaderboardDataResult?>(null)

    val globalLeaderboardDataFlow: StateFlow<LeaderboardDataResult?> get() = _globalLeaderboardDataFlow

    private val _friendsLeaderboardDataFlow = MutableStateFlow<LeaderboardDataResult?>(null)

    val friendsLeaderboardDataFlow: StateFlow<LeaderboardDataResult?> get() = _friendsLeaderboardDataFlow

    fun navigateToUser(id: String) {
        viewModelScope.launch {
            userRouter.goToUser(id)
        }
    }

    fun getResultsOnStart(type: LeaderboardFragment.LeaderboardType, max: Int) {
        when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> getGlobalLeaderboardOnStart(max)
            LeaderboardFragment.LeaderboardType.FRIENDS -> getFriendsLeaderboardOnStart(max)
        }
    }

    private fun getGlobalLeaderboardOnStart(max: Int) {
        viewModelScope.launch {
            try {
                val leaderboard = getGlobalLeaderboardUseCase.invoke(
                    gameModeUiModel = getGameModeUseCase.invoke(),
                    difficultyUiModel = null,
                    categoryUiModel = null,
                    max = max, afterScore = null
                )
                _globalLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _globalLeaderboardDataFlow.value = LeaderboardDataResult.Failure(ex)
            }
        }
    }

    private fun getFriendsLeaderboardOnStart(max: Int) {
        viewModelScope.launch {
            try {
                val leaderboard = getFriendsLeaderboardUseCase.invoke(
                    gameModeUiModel = getGameModeUseCase.invoke(),
                    difficultyUiModel = null,
                    categoryUiModel = null,
                    max = max, afterScore = null
                )
                _friendsLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _friendsLeaderboardDataFlow.value = LeaderboardDataResult.Failure(ex)
            }
        }
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
            LeaderboardFragment.LeaderboardType.FRIENDS -> {}
        }
    }

    private fun loadNextGlobalResults(max: Int, startAfter: Double) {
        viewModelScope.launch {
            try {
                val leaderboard = getGlobalLeaderboardUseCase.invoke(
                    gameModeUiModel = getGameModeUseCase.invoke(),
                    difficultyUiModel = null,
                    categoryUiModel = null,
                    max = max, afterScore = startAfter
                )
                _globalLeaderboardDataFlow.value = LeaderboardDataResult.Success(leaderboard)
            } catch (ex: Throwable) {
                _globalLeaderboardDataFlow.value = LeaderboardDataResult.Failure(ex)
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