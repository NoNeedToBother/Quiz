package ru.kpfu.itis.paramonov.authentication.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.authentication.R
import ru.kpfu.itis.paramonov.authentication.presentation.components.Logo
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenSideEffect
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenState
import ru.kpfu.itis.paramonov.ui.components.ErrorDialog
import ru.kpfu.itis.paramonov.ui.components.InputSection
import ru.kpfu.itis.paramonov.ui.components.PasswordInputSection

@Composable
fun RegisterScreen(
    goToSignInScreen: () -> Unit,
    goToMainMenuScreen: () -> Unit
) {
    val di = localDI()
    val viewModel: RegisterViewModel by di.instance()

    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    var error by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(null) {
        viewModel.checkCurrentUser()

        effect.collect {
            when (it) {
                is RegisterScreenSideEffect.NavigateToMainMenu -> {
                    goToMainMenuScreen()
                }
                is RegisterScreenSideEffect.ShowError -> {
                    val errorMessage = it.message
                    val errorTitle = it.title
                    error = errorTitle to errorMessage
                }
            }
        }
    }

    ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUsernameInput = { viewModel.validateUsername(it) },
        onConfirmPasswordInput = { viewModel.validateConfirmPassword(it) },
        onPasswordInput = { viewModel.validatePassword(it) },
        onEmailInput = { viewModel.validateEmail(it) },
        onRegisterBtnClick = { username, email, password, confirmPassword ->
            viewModel.registerUser(username, email, password, confirmPassword)
        },
        onGoToSignInClick = { goToSignInScreen() }
    )

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

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo()
            InputSections(
                state = state,
                username = username,
                password = password,
                confirmPassword = confirmPassword,
                email = email,
                onUsernameInput = {
                    username = it
                    onUsernameInput(it)
                },
                onConfirmPasswordInput = {
                    confirmPassword = it
                    onConfirmPasswordInput(it)
                },
                onPasswordInput = {
                    password = it
                    onPasswordInput(it)
                },
                onEmailInput = {
                    email = it
                    onEmailInput(it)
                }
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
                enabled = state.value.passwordError != null && state.value.usernameError != null
                        && state.value.confirmPasswordError != null && state.value.emailError != null
                        && username.isNotEmpty() && password.isNotEmpty() &&
                        confirmPassword.isNotEmpty() && email.isNotEmpty()
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

@Composable
fun InputSections(
    state: State<RegisterScreenState>,
    username: String,
    password: String,
    confirmPassword: String,
    email: String,
    onUsernameInput: (String) -> Unit,
    onConfirmPasswordInput: (String) -> Unit,
    onPasswordInput: (String) -> Unit,
    onEmailInput: (String) -> Unit,
) {
    InputSection(
        value = username,
        onInput = {
            onUsernameInput.invoke(username)
        },
        label = stringResource(R.string.enter_username),
        error = state.value.usernameError
    )

    Spacer(modifier = Modifier.height(16.dp))

    InputSection(
        value = email,
        onInput = {
            onEmailInput.invoke(email)
        },
        label = stringResource(R.string.enter_email),
        error = state.value.emailError
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordInputSection(
        value = password,
        onInput = {
            onPasswordInput.invoke(password)
        },
        label = stringResource(R.string.enter_password),
        error = state.value.passwordError
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordInputSection(
        value = confirmPassword,
        onInput = {
            onConfirmPasswordInput.invoke(confirmPassword)
        },
        label = stringResource(R.string.confirm_password),
        error = state.value.confirmPasswordError
    )
}
