package ru.kpfu.itis.paramonov.users.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import ru.kpfu.itis.paramonov.ui.views.EmptyResults
import ru.kpfu.itis.paramonov.users.R
import ru.kpfu.itis.paramonov.users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.users.presentation.mvi.SearchUsersScreenSideEffect
import ru.kpfu.itis.paramonov.users.presentation.mvi.SearchUsersScreenState
import ru.kpfu.itis.paramonov.users.presentation.ui.components.SearchBar
import ru.kpfu.itis.paramonov.users.presentation.ui.components.UserList
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.SearchUsersViewModel
import java.lang.IllegalStateException
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

class SearchUsersScreen: MviBaseFragment() {

    @Inject
    lateinit var viewModel: SearchUsersViewModel

    @Inject
    lateinit var userRouter: UserRouter

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .searchUsersComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                val state = viewModel.container.stateFlow.collectAsState()
                val effect = viewModel.container.sideEffectFlow

                var lastTime by remember { mutableLongStateOf(DEFAULT_LAST_TIME_VALUE) }
                var timer by remember { mutableStateOf(Timer()) }

                LaunchedEffect(null) {
                    effect.collect {
                        when (it) {
                            is SearchUsersScreenSideEffect.ShowError -> {
                                val errorMessage = it.message
                                val errorTitle = getString(R.string.search_users_fail)

                                showErrorBottomSheetDialog(errorTitle, errorMessage)
                            }
                        }
                    }
                }

                AppTheme {
                    var searchQuery by remember { mutableStateOf("") }
                    Screen(
                        state = state,
                        onUserClick = { id -> userRouter.goToUser(id) },
                        onSearch = {
                            searchQuery = it
                            val currentTime = System.currentTimeMillis()
                            if (lastTime != DEFAULT_LAST_TIME_VALUE &&
                                currentTime - lastTime < MIN_TIME_BETWEEN_REGISTERING
                            ) {
                                try {
                                    timer.cancel()
                                    timer = Timer()
                                } catch (_: IllegalStateException) {}
                            }
                            lastTime = currentTime
                            timer.schedule(timerTask {
                                viewModel.searchUsers(it, MAX_USER_AMOUNT, null)
                            }, MIN_TIME_BETWEEN_REGISTERING)
                            viewModel.searchUsers(it, MAX_USER_AMOUNT, null)
                        },
                        loadMore = {
                            val last = state.value.users.last()
                            viewModel.loadNextUsers(searchQuery, MAX_USER_AMOUNT, last.id)
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val MAX_USER_AMOUNT = 15

        private const val MIN_TIME_BETWEEN_REGISTERING = 400L

        private const val DEFAULT_LAST_TIME_VALUE = -1L
    }
}

@Composable
fun Screen(
    state: State<SearchUsersScreenState>,
    onUserClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    loadMore: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        var searchQuery by remember { mutableStateOf("") }

        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                onSearch(searchQuery)
            },
        )

        if (state.value.users.isEmpty() && state.value.loadingEnded) {
            EmptyResults()
        } else {
            UserList(
                friends = state.value.users,
                onFriendClick = onUserClick,
                shouldLoadMore = { loadMore() }
            )
        }
    }
}
