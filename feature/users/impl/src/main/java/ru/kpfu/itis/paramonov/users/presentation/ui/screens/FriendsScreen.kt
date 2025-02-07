package ru.kpfu.itis.paramonov.users.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.ui.components.EmptyResults
import ru.kpfu.itis.paramonov.users.R
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsScreenState
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsScreenSideEffect
import ru.kpfu.itis.paramonov.users.presentation.ui.components.UserList
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.FriendsViewModel

/*class FriendsScreen: MviBaseFragment(), DIAware {

    override val di: DI by closestDI()

    private val viewModel: FriendsViewModel by viewModel()

    private val userRouter: UserRouter by instance()

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {

            }
        }
    }

    companion object {
        private const val MAX_USER_AMOUNT = 15
    }
}*/

private const val MAX_USER_AMOUNT = 15

@Composable
fun FriendsScreen(
    goToUserScreen: (String) -> Unit
) {
    val di = localDI()
    val viewModel: FriendsViewModel by di.instance()
    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    LaunchedEffect(null) {
        viewModel.getFriends(MAX_USER_AMOUNT)

        effect.collect {
            when (it) {
                is FriendsScreenSideEffect.ShowError -> {
                    //val errorMessage = it.message
                    //val errorTitle = getString(R.string.get_friends_fail)

                    //showErrorBottomSheetDialog(errorTitle, errorMessage)
                }
            }
        }
    }

    ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onFriendClick = { id -> goToUserScreen(id) },
        loadFriends = { offset ->
            viewModel.loadNextFriends(offset, MAX_USER_AMOUNT)
        }
    )
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    state: State<FriendsScreenState>,
    onFriendClick: (String) -> Unit,
    loadFriends: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.your_friends),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        if (state.value.friends.isEmpty() && state.value.loadingEnded) {
            EmptyResults()
        } else {
            UserList(
                friends = state.value.friends,
                onFriendClick = onFriendClick,
                shouldLoadMore = loadFriends
            )
        }
    }
}
