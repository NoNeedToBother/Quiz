package ru.kpfu.itis.paramonov.leaderboards.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.R
import ru.kpfu.itis.paramonov.leaderboards.presentation.ui.components.ResultList
import ru.kpfu.itis.paramonov.leaderboards.presentation.ui.components.SettingsBottomSheetWrapper
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenSideEffect
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenState
import ru.kpfu.itis.paramonov.leaderboards.presentation.viewmodel.LeaderboardsViewModel
import ru.kpfu.itis.paramonov.ui.components.EmptyResults

private const val LEADERBOARD_MAX_AT_ONCE = 15

private const val LEADERBOARD_ABSOLUTE_MAX = 500

@Composable
fun LeaderboardsScreen(
    goToUserScreen: (String) -> Unit
) {
    val di = localDI()
    val viewModel: LeaderboardsViewModel by di.instance()
    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    LaunchedEffect(null) {
        viewModel.getGlobalLeaderboard(LEADERBOARD_MAX_AT_ONCE)

        effect.collect {
            when (it) {
                is LeaderboardsScreenSideEffect.ShowError -> {
                    //val errorMessage = it.message
                    //val errorTitle = getString(R.string.get_results_fail)

                    //showErrorBottomSheetDialog(errorTitle, errorMessage)
                }
            }
        }
    }

    ScreenContent(
        state = state,
        onFriendLeaderboardChosen = {
            viewModel.getFriendsLeaderboard(LEADERBOARD_MAX_AT_ONCE)
        },
        onGlobalLeaderboardChosen = {
            viewModel.getGlobalLeaderboard(LEADERBOARD_MAX_AT_ONCE)
        },
        onProfileClick = { goToUserScreen(it) },
        loadMoreGlobalResults = { scoreAfter ->
            if (state.value.results.size < LEADERBOARD_ABSOLUTE_MAX)
                viewModel.loadNextGlobalResults(LEADERBOARD_MAX_AT_ONCE, scoreAfter)
        },
        loadMoreFriendResults = { scoreAfter ->
            if (state.value.results.size < LEADERBOARD_ABSOLUTE_MAX)
                viewModel.loadNextFriendsResults(
                    LEADERBOARD_MAX_AT_ONCE,
                    scoreAfter
                )
        },
        onSaveClick = { viewModel.saveSettings() },
        onDifficultyChosen = { viewModel.changeDifficulty(it) },
        onCategoryChosen = { viewModel.changeCategory(it) },
        onGameModeChosen = { viewModel.changeGameMode(it) }
    )
}

@Composable
fun ScreenContent(
    state: State<LeaderboardsScreenState>,
    onGlobalLeaderboardChosen: () -> Unit,
    onFriendLeaderboardChosen: () -> Unit,
    onProfileClick: (String) -> Unit,
    loadMoreFriendResults: (Double) -> Unit,
    loadMoreGlobalResults: (Double) -> Unit,
    onSaveClick: () -> Unit,
    onDifficultyChosen: (String) -> Unit,
    onCategoryChosen: (String) -> Unit,
    onGameModeChosen: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.global_leaderboard), stringResource(R.string.friend_leaderboard))

    SettingsBottomSheetWrapper(
        onSaveClick = {
            onSaveClick()
            if (selectedTabIndex == 0) onGlobalLeaderboardChosen()
            else onFriendLeaderboardChosen()
        },
        onDifficultyChosen = onDifficultyChosen,
        onCategoryChosen = onCategoryChosen,
        onGameModeChosen = onGameModeChosen,
        difficultyValue = state.value.settings?.difficulty?.toString()?.normalizeEnumName()
            ?: stringResource(R.string.any_settings_item),
        categoryValue = state.value.settings?.category?.toString()?.normalizeEnumName()
            ?: stringResource(R.string.any_settings_item),
        gameModeValue = state.value.settings?.gameMode.toString().normalizeEnumName(),
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = {
                            if (index == 0) onGlobalLeaderboardChosen()
                            else onFriendLeaderboardChosen()
                            selectedTabIndex = index
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> Leaderboard(
                    state = state,
                    onProfileClick = onProfileClick,
                    loadMore = { loadMoreGlobalResults(state.value.results.minBy { it.score }.score) }
                )
                1 -> Leaderboard(
                    state = state,
                    onProfileClick = onProfileClick,
                    loadMore = { loadMoreFriendResults(state.value.results.minBy { it.score }.score) }
                )
            }
        }
    }
}

@Composable
fun Leaderboard(
    state: State<LeaderboardsScreenState>,
    onProfileClick: (String) -> Unit,
    loadMore: () -> Unit
) {
    Box {
        if (state.value.results.isEmpty() && state.value.loadingEnded) {
            EmptyResults(modifier = Modifier.align(Alignment.TopCenter))
        } else {
            ResultList(
                modifier = Modifier.align(Alignment.TopCenter),
                results = state.value.results,
                onProfileClick = onProfileClick,
                shouldLoadMore = { loadMore() }
            )
        }
    }
}
