package ru.kpfu.itis.paramonov.leaderboards.presentation.fragments

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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import ru.kpfu.itis.paramonov.leaderboards.R
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsComponent
import ru.kpfu.itis.paramonov.leaderboards.di.FeatureLeaderboardsDependencies
import ru.kpfu.itis.paramonov.leaderboards.presentation.components.ResultList
import ru.kpfu.itis.paramonov.leaderboards.presentation.components.SettingsBottomSheetWrapper
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenSideEffect
import ru.kpfu.itis.paramonov.leaderboards.presentation.mvi.LeaderboardsScreenState
import ru.kpfu.itis.paramonov.leaderboards.presentation.viewmodel.LeaderboardsViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import ru.kpfu.itis.paramonov.ui.views.EmptyResults
import javax.inject.Inject

class LeaderboardScreen: MviBaseFragment() {

    @Inject
    lateinit var viewModel: LeaderboardsViewModel

    @Inject
    lateinit var userRouter: UserRouter

    override fun inject() {
        FeatureUtils.getFeature<FeatureLeaderboardsComponent>(this, FeatureLeaderboardsDependencies::class.java)
            .leaderboardComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                val state = viewModel.container.stateFlow.collectAsState()
                val effect = viewModel.container.sideEffectFlow

                LaunchedEffect(null) {
                    viewModel.getGlobalLeaderboard(LEADERBOARD_MAX_AT_ONCE)

                    effect.collect {
                        when (it) {
                            is LeaderboardsScreenSideEffect.ShowError -> {
                                val errorMessage = it.message
                                val errorTitle = getString(R.string.get_results_fail)

                                showErrorBottomSheetDialog(errorTitle, errorMessage)
                            }
                        }
                    }
                }

                AppTheme {
                    Screen(
                        state = state,
                        onFriendLeaderboardChosen = {
                            viewModel.getFriendsLeaderboard(LEADERBOARD_MAX_AT_ONCE)
                        },
                        onGlobalLeaderboardChosen = {
                            viewModel.getGlobalLeaderboard(LEADERBOARD_MAX_AT_ONCE)
                        },
                        onProfileClick = { userRouter.goToUser(it) },
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
            }
        }
    }

    companion object {
        private const val LEADERBOARD_MAX_AT_ONCE = 15

        private const val LEADERBOARD_ABSOLUTE_MAX = 500
    }
}

@Composable
fun Screen(
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
        onGameModeChosen = onGameModeChosen
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
