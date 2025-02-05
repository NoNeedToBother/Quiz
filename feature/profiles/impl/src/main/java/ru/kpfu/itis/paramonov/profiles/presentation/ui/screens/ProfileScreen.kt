package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesComponent
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependencies
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenState
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.ProfileInfoField
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.ConfirmCredentialsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.ProfileCredentialsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.ProfilePictureDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.RequestsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.StatsDialog
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.ProfileViewModel
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import javax.inject.Inject

class ProfileScreen: MviBaseFragment() {

    @Inject
    lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var usernameValidator: UsernameValidator

    @Inject
    lateinit var passwordValidator: PasswordValidator

    @Inject
    lateinit var authenticationRouter: AuthenticationRouter

    private val pickProfilePictureIntent = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
                uri -> onProfilePictureChosen(uri)
        }
    }

    private fun onProfilePictureChosen(uri: Uri) {
        ProfilePictureDialogFragment.builder()
            .setOnPositivePressed {
                viewModel.saveProfilePicture(uri)
            }
            .setImageSource(uri)
            .create()
            .show(childFragmentManager,
                ProfilePictureDialogFragment.PROFILE_PICTURE_DIALOG_TAG)
    }

    private fun onLastResultsDataReceived(list: List<ResultUiModel>) {
        StatsDialog.builder()
            .provideResultList(list)
            .build()
            .show(childFragmentManager, StatsDialog.STATS_DIALOG_TAG)
    }

    private fun onRequestsDataReceived(list: List<UserModel>) {
        RequestsDialogFragment.builder()
            .provideRequestList(list)
            .setOnRequestAccepted {
                viewModel.acceptFriendRequest(it)
            }
            .setOnRequestDenied {
                viewModel.denyFriendRequest(it)
            }
            .build()
            .show(childFragmentManager, RequestsDialogFragment.REQUESTS_DIALOG_TAG)
    }

    private fun onChangeCredentialsClicked() {
        //setChangeCredentialMenuItemEnabled(false)
        ConfirmCredentialsDialogFragment.builder()
            .setOnPositivePressed(object : ConfirmCredentialsDialogFragment.OnCredentialsChangedListener {
                override fun onCredentialsChanged(email: String?, password: String?) {
                    if (email != null && password != null) viewModel.confirmCredentials(email, password)
                    else onConfirmCredentialsFailed()
                }
            })
            .setOnDismiss {
                onDialogDismiss()
            }
            .build()
            .show(childFragmentManager, ConfirmCredentialsDialogFragment.CONFIRM_CREDENTIALS_DIALOG_TAG)
    }

    private fun onDialogDismiss() {
        //setChangeCredentialMenuItemEnabled(true)
    }

    private fun onUserSettingsClicked() {
        ProfileSettingsDialogFragment.builder()
            .setOnPositivePressed {
                viewModel.saveUserSettings(it)
            }
            .setUsernameValidator(usernameValidator)
            .build()
            .show(childFragmentManager, ProfileSettingsDialogFragment.SETTINGS_DIALOG_TAG)
    }

    private fun onConfirmCredentialsFailed() {
        showErrorBottomSheetDialog(
            getString(R.string.dialog_incorrect_credentials),
            getString(R.string.dialog_incorrect_credentials_expanded)
        )
    }

    private fun onCredentialsConfirmed() {
        ProfileCredentialsDialogFragment.builder()
            .setOnPositivePressed(object : ProfileCredentialsDialogFragment.OnCredentialsChangedListener {
                override fun onCredentialsChanged(
                    email: String?,
                    password: String?,
                    confirmPassword: String?
                ) {
                    if (password != null && confirmPassword != null) {
                        if (password == confirmPassword) viewModel.changeCredentials(email, password)
                        else showErrorBottomSheetDialog(
                            getString(R.string.dialog_incorrect_credentials),
                            getString(R.string.password_confirm_password_not_match)
                        )
                    }
                    else if (password == null && confirmPassword == null) {
                        viewModel.changeCredentials(email, null)
                    } else showErrorBottomSheetDialog(
                        getString(R.string.credentials_change_failed),
                        getString(R.string.dialog_incorrect_credentials)
                    )
                }
            })
            .setOnDismiss {
                onDialogDismiss()
            }
            .setPasswordValidator(passwordValidator)
            .build()
            .show(childFragmentManager, ProfileCredentialsDialogFragment.CREDENTIALS_DIALOG_TAG)
    }

    override fun inject() {
        FeatureUtils.getFeature<FeatureProfilesComponent>(this, FeatureProfilesDependencies::class.java)
            .profileComponentFactory()
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
                        viewModel.getCurrentUser()
                        viewModel.subscribeToProfileUpdates()

                        effect.collect {
                            when(it) {
                                ProfileScreenSideEffect.CredentialsConfirmed -> {
                                    onCredentialsConfirmed()
                                }
                                is ProfileScreenSideEffect.FriendRequestsReceived -> {
                                    onRequestsDataReceived(it.requests)
                                }
                                ProfileScreenSideEffect.GoToSignIn -> { authenticationRouter.goToSignIn() }
                                is ProfileScreenSideEffect.ResultsReceived -> {
                                    onLastResultsDataReceived(it.results)
                                }
                                is ProfileScreenSideEffect.ShowError -> {
                                    val errorMessage = it.message
                                    val errorTitle = getString(R.string.get_info_fail)

                                    showErrorBottomSheetDialog(errorTitle, errorMessage)
                                }
                            }
                        }
                    }

                    Screen(
                        state = state,
                        onSubmitPhotoClick = {
                            pickProfilePictureIntent.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onCheckResultsClick = { viewModel.getLastResults(MAX_RESULTS_AMOUNT) },
                        onCheckRequestsClick = { viewModel.getFriendRequests() },
                        onUserSettingsClicked = { onUserSettingsClicked() },
                        onChangeCredentialsClicked = { onChangeCredentialsClicked() },
                        onLogoutClicked = { viewModel.logout() }
                    )
                }
            }
        }
    }

    companion object {
        private const val MAX_RESULTS_AMOUNT = 10
    }
}

@Composable
fun Screen(
    state: State<ProfileScreenState>,
    onSubmitPhotoClick: () -> Unit,
    onCheckRequestsClick: () -> Unit,
    onCheckResultsClick: () -> Unit,
    onUserSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onChangeCredentialsClicked: () -> Unit
) {
    Box {
        SettingsMenu(
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            onUserSettingsClicked = onUserSettingsClicked,
            onLogoutClicked = onLogoutClicked,
            onChangeCredentialsClicked = onChangeCredentialsClicked
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(top = 24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(
                imageUrl = state.value.user?.profilePictureUrl,
                onSubmitPhotoClick = onSubmitPhotoClick
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
                label = stringResource(R.string.registration_date),
                value = state.value.user?.dateRegistered ?: ""
            )

            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp),
                    onClick = onCheckResultsClick
                ) {
                    Text(stringResource(R.string.check_stats))
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp),
                    onClick = onCheckRequestsClick
                ) {
                    Text(stringResource(R.string.check_requests))
                }
            }
        }
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    onSubmitPhotoClick: () -> Unit
) {
    Box(modifier = modifier.size(200.dp), contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        FloatingActionButton(onClick = onSubmitPhotoClick, modifier = Modifier.size(64.dp)) {
            Icon(Icons.Default.Photo, contentDescription = stringResource(R.string.submit_photo))
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    onUserSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onChangeCredentialsClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.settings_anim)
    var atEnd by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = stringResource(R.string.settings),
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    expanded = true
                    atEnd = !atEnd
                }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
            atEnd = false
        }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.settings)) },
                onClick = {
                    expanded = false
                    atEnd = false
                    onUserSettingsClicked()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.change_credentials)) },
                onClick = {
                    expanded = false
                    atEnd = false
                    onChangeCredentialsClicked()
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.logout), color = Color.Red) },
                onClick = {
                    expanded = false
                    atEnd = false
                    onLogoutClicked()
                }
            )
        }
    }
}
