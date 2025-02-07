package ru.kpfu.itis.paramonov.authentication.presentation.signing_in

import androidx.lifecycle.ViewModel
import ru.kpfu.itis.paramonov.authentication.api.usecase.AuthenticateUserUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi.SignInScreenSideEffect
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.mvi.SignInScreenState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator

class SignInViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
    private val mapper: UserUiModelMapper,
    private val passwordValidator: PasswordValidator
): ViewModel(), ContainerHost<SignInScreenState, SignInScreenSideEffect> {

    override val container = container<SignInScreenState, SignInScreenSideEffect>(SignInScreenState(null))

    fun authenticateUser(username: String, password: String) = intent {
        reduce { state.copy(isLoading = true) }

        try {
            val user = mapper.map(
                authenticateUserUseCase.invoke(username, password)
            )

            reduce { state.copy(userData = user, isLoading = false) }
            postSideEffect(SignInScreenSideEffect.NavigateToMainMenu)
        } catch (ex: Throwable) {
            reduce { state.copy(userData = null, isLoading = false) }
            postSideEffect(SignInScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun checkCurrentUser() = intent {
        try {
            val user = checkUserIsAuthenticatedUseCase.invoke()?.let { mapper.map(it) }
            user?.let {
                reduce { state.copy(userData = it) }
                postSideEffect(SignInScreenSideEffect.NavigateToMainMenu)
            }
        } catch (ex: Throwable) {
            reduce { state.copy(userData = null) }
            postSideEffect(SignInScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun validatePassword(password: String) = intent {
        reduce { state.copy(isPasswordCorrect = passwordValidator.validate(password)) }
    }

    fun validateEmail(email: String) = intent {
        reduce { state.copy(isEmailCorrect = email.contains("@")) }
    }
}
