package ru.kpfu.itis.paramonov.authentication.presentation.signing_in

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
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi.SignInScreenSideEffect
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi.SignInScreenState
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme

class SignInScreen: MviBaseFragment(), DIAware {

    override val di: DI by closestDI()

    private val mainMenuRouter: MainMenuRouter by instance()

    private val authenticationRouter: AuthenticationRouter by instance()

    private val viewModel: SignInViewModel by viewModel()

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                val state = viewModel.container.stateFlow.collectAsState()
                val effect = viewModel.container.sideEffectFlow

                LaunchedEffect(null) {
                    viewModel.checkCurrentUser()

                    effect.collect {
                        when (it) {
                            is SignInScreenSideEffect.NavigateToMainMenu -> {
                                mainMenuRouter.goToMainMenu()
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.welcome_back_user, state.value.userData?.username),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is SignInScreenSideEffect.ShowError -> {
                                val errorMessage = it.message
                                val errorTitle = getString(R.string.sign_in_failed)

                                showErrorBottomSheetDialog(errorTitle, errorMessage)
                            }
                        }
                    }
                }

                AppTheme {
                    Screen(
                        state = state,
                        onEmailInput = { viewModel.validateEmail(it) },
                        onPasswordInput = { viewModel.validatePassword(it) },
                        onSignInBtnClick = { email, password ->
                            viewModel.authenticateUser(email, password)
                        },
                        onGoToRegisterClick = { authenticationRouter.goToRegister() }
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(
    state: State<SignInScreenState>,
    onEmailInput: (String) -> Unit,
    onPasswordInput: (String) -> Unit,
    onSignInBtnClick: (String, String) -> Unit,
    onGoToRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    onSignInBtnClick.invoke(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.isPasswordCorrect && email.contains("@")
            ) {
                Text(stringResource(R.string.sign_in))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onGoToRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.go_to_register))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
