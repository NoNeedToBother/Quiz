package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardFragment
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel

class LeaderboardsViewModel(
    private val getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
    private val getGameModeUseCase: GetGameModeUseCase
): BaseViewModel() {

    private val _resultDataFlow = MutableStateFlow<LeaderboardDataResult?>(null)

    val resultDataFlow: StateFlow<LeaderboardDataResult?> get() = _resultDataFlow

    fun getResultsOnStart(type: LeaderboardFragment.LeaderboardType) {
        when(type) {
            LeaderboardFragment.LeaderboardType.GLOBAL -> getGlobalLeaderboardOnStart()
            LeaderboardFragment.LeaderboardType.FRIENDS -> getFriendsLeaderboardOnStart()
        }
    }

    private fun getGlobalLeaderboardOnStart() {
        viewModelScope.launch {
            val res = getGlobalLeaderboardUseCase.invoke(
                gameModeUiModel = getGameModeUseCase.invoke(), null, null
            )
            Log.i("a", res.toString())
        }
    }

    private fun getFriendsLeaderboardOnStart() {

    }

    sealed interface LeaderboardDataResult: Result {
        class Success(private val result: ResultUiModel): LeaderboardDataResult, Result.Success<ResultUiModel> {
            override fun getValue(): ResultUiModel = result
        }

        class Failure(private val ex: Throwable): LeaderboardDataResult, Result.Failure {
            override fun getException(): Throwable = ex

        }
    }
}