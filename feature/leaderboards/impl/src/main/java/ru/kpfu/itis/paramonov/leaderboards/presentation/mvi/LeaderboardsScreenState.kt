package ru.kpfu.itis.paramonov.leaderboards.presentation.mvi

import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.SettingUiModel

data class LeaderboardsScreenState(
    val results: List<ResultUiModel> = emptyList(),
    val settings: SettingUiModel? = null,
    val loadingEnded: Boolean = false
)