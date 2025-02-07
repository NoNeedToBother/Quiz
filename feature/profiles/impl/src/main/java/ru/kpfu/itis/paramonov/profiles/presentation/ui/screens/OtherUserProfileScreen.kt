package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.StatsDialog
import ru.kpfu.itis.paramonov.profiles.presentation.model.FriendStatusUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenState
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.ProfileInfoField
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.OtherUserProfileViewModel
import ru.kpfu.itis.paramonov.ui.components.ErrorDialog

private const val MAX_RESULTS_AMOUNT = 10

@Composable
fun OtherUserProfileScreen(
    userId: String
) {
    val di = localDI()
    val viewModel: OtherUserProfileViewModel by di.instance()
    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    var results by remember { mutableStateOf<List<ResultUiModel>?>(null) }
    var error by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(null) {
        viewModel.getUser(userId)
        viewModel.checkFriendStatus(userId)

        effect.collect {
            when (it) {
                is OtherUserProfileScreenSideEffect.ShowError -> {
                    val errorMessage = it.message
                    val errorTitle = it.title

                    error = errorTitle to errorMessage
                }
                is OtherUserProfileScreenSideEffect.ResultsReceived ->
                    results = it.results
            }
        }
    }

    ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onGetResultsClicked = { viewModel.getLastResults(MAX_RESULTS_AMOUNT, userId) },
        onAddFriendClick = { viewModel.sendFriendRequest(userId) }
    ) {
        results?.let {
            StatsDialog(
                results = it,
                onDismiss = {
                    results = null
                }
            )
        }
    }

    Box {
        error?.let {
            ErrorDialog(
                onDismiss = { error = null },
                title = it.first,
                text = it.second
            )
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    state: State<OtherUserProfileScreenState>,
    onGetResultsClicked: () -> Unit,
    onAddFriendClick: () -> Unit,
    dialogs: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(PaddingValues(top = 24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(state.value.user?.profilePictureUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp).clip(CircleShape)
        )
        ProfileInfoField(
            modifier = Modifier.padding(PaddingValues(top = 12.dp, start = 80.dp, end = 80.dp)),
            label = stringResource(R.string.username),
            value = state.value.user?.username ?: ""
        )
        ProfileInfoField(
            modifier = Modifier.padding(PaddingValues(top = 12.dp, start = 80.dp, end = 80.dp)),
            label = stringResource(R.string.info),
            value = state.value.user?.info ?: ""
        )
        ProfileInfoField(
            modifier = Modifier.padding(PaddingValues(top = 12.dp, start = 80.dp, end = 80.dp)),
            label = stringResource(R.string.registration_date, state.value.user?.dateRegistered ?: ""),
            value = ""
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onGetResultsClicked() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp)
            ) {
                Text(text = stringResource(R.string.check_stats))
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (state.value.friendStatus != null) {
                FriendStatusButton(
                    status = state.value.friendStatus,
                    onAddFriendClick = onAddFriendClick,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp),
                    requestSent = state.value.friendRequestSent
                )
            }
        }
    }
    dialogs()
}

@Composable
fun FriendStatusButton(
    modifier: Modifier = Modifier,
    status: FriendStatusUiModel?,
    requestSent: Boolean?,
    onAddFriendClick: () -> Unit
) {
    requestSent?.let {
        if (it) {
            OutlinedButton(
                modifier = modifier,
                onClick = {}
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.request_sent),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(R.drawable.request_sent),
                        contentDescription = ""
                    )
                }
            }
        }
    } ?:
    when (status) {
        FriendStatusUiModel.NOT_FRIEND -> Button(
            modifier = modifier,
            onClick = onAddFriendClick
        ) {
            Row {
                Text(
                    text = stringResource(R.string.add_friend),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.add_friend),
                    contentDescription = ""
                )
            }
        }
        FriendStatusUiModel.REQUEST_SENT -> OutlinedButton(
            modifier = modifier,
            onClick = {}
        ) {
            Row {
                Text(
                    text = stringResource(R.string.request_sent),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.request_sent),
                    contentDescription = ""
                )
            }
        }
        FriendStatusUiModel.FRIEND -> OutlinedButton(
            modifier = modifier,
            onClick = {}
        ) {
            Row {
                Text(
                    text = stringResource(R.string.is_friend),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.is_friend),
                    contentDescription = ""
                )
            }
        }
        else -> {}
    }
}
