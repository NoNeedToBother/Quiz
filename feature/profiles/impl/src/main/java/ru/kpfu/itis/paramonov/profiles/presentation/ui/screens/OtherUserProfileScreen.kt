package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesComponent
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependencies
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.StatsDialog
import ru.kpfu.itis.paramonov.profiles.presentation.model.FriendStatusUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenState
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.ProfileInfoField
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.OtherUserProfileViewModel
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import javax.inject.Inject

class OtherUserProfileScreen: MviBaseFragment() {

    @Inject
    lateinit var viewModel: OtherUserProfileViewModel

    private val userId: String get() {
        val args = requireArguments()
        return args.getString(USER_ID_KEY) ?: ""
    }

    override fun inject() {
        FeatureUtils.getFeature<FeatureProfilesComponent>(this, FeatureProfilesDependencies::class.java)
            .otherUserProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val state = viewModel.container.stateFlow.collectAsState()
                    val effect = viewModel.container.sideEffectFlow

                    LaunchedEffect(null) {
                        viewModel.getUser(userId)
                        viewModel.checkFriendStatus(userId)

                        effect.collect {
                            when (it) {
                                is OtherUserProfileScreenSideEffect.ShowError -> {
                                    val errorMessage = it.message
                                    val errorTitle = getString(R.string.get_info_fail)

                                    showErrorBottomSheetDialog(errorTitle, errorMessage)
                                }
                                is OtherUserProfileScreenSideEffect.ResultsReceived ->
                                    StatsDialog.builder()
                                        .provideResultList(it.results)
                                        .build()
                                        .show(childFragmentManager, StatsDialog.STATS_DIALOG_TAG)
                            }
                        }
                    }

                    Screen(
                        state = state,
                        onGetResultsClicked = { viewModel.getLastResults(MAX_RESULTS_AMOUNT, userId) },
                        onAddFriendClick = { viewModel.sendFriendRequest(userId) }
                    )
                }
            }
        }
    }

    companion object {
        const val USER_ID_KEY = "id"
        private const val MAX_RESULTS_AMOUNT = 10
    }
}

@Composable
fun Screen(
    state: State<OtherUserProfileScreenState>,
    onGetResultsClicked: () -> Unit,
    onAddFriendClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(PaddingValues(top = 24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(state.value.user?.profilePictureUrl),
            contentDescription = null,
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
                Text(text = stringResource(R.string.request_sent))
            }
        }
    } ?:
    when (status) {
        FriendStatusUiModel.NOT_FRIEND -> Button(
            modifier = modifier,
            onClick = onAddFriendClick
        ) {
            Text(text = stringResource(R.string.add_friend))
        }
        FriendStatusUiModel.REQUEST_SENT -> OutlinedButton(
            modifier = modifier,
            onClick = {}
        ) {
            Text(text = stringResource(R.string.request_sent))
        }
        FriendStatusUiModel.FRIEND -> OutlinedButton(
            modifier = modifier,
            onClick = {}
        ) {
            Text(text = stringResource(R.string.is_friend))
        }
        else -> {}
    }
}
