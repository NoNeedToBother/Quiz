package ru.kpfu.itis.paramonov.authentication.presentation.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.authentication.R
import ru.kpfu.itis.paramonov.authentication.presentation.components.InputSection
import ru.kpfu.itis.paramonov.authentication.presentation.components.Logo
import ru.kpfu.itis.paramonov.authentication.presentation.components.PasswordSection
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenSideEffect
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenState
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme

class RegisterScreen: MviBaseFragment(), DIAware {

    override val di: DI by closestDI()

    private val mainMenuRouter: MainMenuRouter by instance()

    private val authenticationRouter: AuthenticationRouter by instance()

    private val viewModel: RegisterViewModel by viewModel()

    override fun inject() {
    }

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                val state = viewModel.container.stateFlow.collectAsState()
                val effect = viewModel.container.sideEffectFlow

                LaunchedEffect(null) {
                    viewModel.checkCurrentUser()

                    effect.collect {
                        when (it) {
                            is RegisterScreenSideEffect.NavigateToMainMenu -> {
                                mainMenuRouter.goToMainMenu()
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.welcome_user, state.value.userData?.username),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is RegisterScreenSideEffect.ShowError -> {
                                val errorMessage = it.message
                                val errorTitle = getString(R.string.registration_failed)

                                showErrorBottomSheetDialog(errorTitle, errorMessage)
                            }
                        }
                    }
                }

                AppTheme {
                    Screen(
                        state = state,
                        onUsernameInput = { viewModel.validateUsername(it) },
                        onConfirmPasswordInput = { viewModel.validateConfirmPassword(it) },
                        onPasswordInput = { viewModel.validatePassword(it) },
                        onEmailInput = { viewModel.validateEmail(it) },
                        onRegisterBtnClick = { username, email, password, confirmPassword ->
                            viewModel.registerUser(username, email, password, confirmPassword)
                        },
                        onGoToSignInClick = { authenticationRouter.goToSignIn() }
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(
    state: State<RegisterScreenState>,
    onUsernameInput: (String) -> Unit,
    onConfirmPasswordInput: (String) -> Unit,
    onPasswordInput: (String) -> Unit,
    onEmailInput: (String) -> Unit,
    onRegisterBtnClick: (String, String, String, String) -> Unit,
    onGoToSignInClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo()
            InputSection(
                value = username,
                onValueChange = {
                    username = it
                    onUsernameInput.invoke(username)
                },
                label = stringResource(R.string.enter_username)
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputSection(
                value = email,
                onValueChange = {
                    email = it
                    onEmailInput.invoke(email)
                },
                label = stringResource(R.string.enter_email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordSection(
                value = password,
                onValueChange = {
                    password = it
                    onPasswordInput.invoke(password)
                },
                label = stringResource(R.string.enter_password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordSection(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    onConfirmPasswordInput.invoke(confirmPassword)
                },
                label = stringResource(R.string.confirm_password)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(80.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    onRegisterBtnClick.invoke(
                        username, email, password, confirmPassword
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.isPasswordCorrect && state.value.isUsernameCorrect
                        && state.value.isConfirmPasswordCorrect && state.value.isEmailCorrect
            ) {
                Text(stringResource(R.string.register))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onGoToSignInClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.go_to_sign_in))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
