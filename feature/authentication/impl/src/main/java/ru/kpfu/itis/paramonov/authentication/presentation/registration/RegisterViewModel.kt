package ru.kpfu.itis.paramonov.authentication.presentation.registration

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenState
import ru.kpfu.itis.paramonov.authentication.presentation.registration.mvi.RegisterScreenSideEffect
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
    private val mapper: UserUiModelMapper,
    private val usernameValidator: UsernameValidator,
    private val passwordValidator: PasswordValidator
): ViewModel(), ContainerHost<RegisterScreenState, RegisterScreenSideEffect> {

    override val container = container<RegisterScreenState, RegisterScreenSideEffect>(RegisterScreenState(null))

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) = intent {
        reduce { state.copy(isLoading = true) }

        try {
            val user = mapper.map(
                registerUserUseCase.invoke(username, email, password, confirmPassword)
            )

            reduce { state.copy(userData = user, isLoading = false) }
            postSideEffect(RegisterScreenSideEffect.NavigateToMainMenu)
        } catch (ex: Throwable) {
            reduce { state.copy(userData = null, isLoading = false) }
            postSideEffect(RegisterScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun checkCurrentUser() = intent {
        try {
            val user = checkUserIsAuthenticatedUseCase.invoke()?.let { mapper.map(it) }
            user?.let {
                reduce { state.copy(userData = it) }
                postSideEffect(RegisterScreenSideEffect.NavigateToMainMenu)
            }
        } catch (ex: Throwable) {
            reduce { state.copy(userData = null) }
            postSideEffect(RegisterScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun validatePassword(password: String) = intent {
        reduce { state.copy(isPasswordCorrect = passwordValidator.validate(password)) }
    }

    fun validateConfirmPassword(password: String) = intent {
        reduce { state.copy(isConfirmPasswordCorrect = passwordValidator.validate(password)) }
    }

    fun validateUsername(username: String) = intent {
        reduce { state.copy(isUsernameCorrect = usernameValidator.validate(username)) }
    }

    fun validateEmail(email: String) = intent {
        reduce { state.copy(isEmailCorrect = email.contains("@")) }
    }
}