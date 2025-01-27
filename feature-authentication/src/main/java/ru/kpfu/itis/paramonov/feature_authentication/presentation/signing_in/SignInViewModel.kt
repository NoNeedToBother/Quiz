package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.AuthenticateUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter

class SignInViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
    private val authenticationRouter: AuthenticationRouter,
    private val mainMenuRouter: MainMenuRouter
): BaseViewModel() {

    private val _userDataFlow = MutableStateFlow<UserDataResult?>(null)

    val userDataFlow: StateFlow<UserDataResult?> get() = _userDataFlow

    private val _signInProceedingFlow = MutableStateFlow(false)
    val signInProceedingFlow: StateFlow<Boolean> get() = _signInProceedingFlow

    fun authenticateUser(username: String, password: String) {
        viewModelScope.launch {
            _signInProceedingFlow.value = true
            try {
                val user = authenticateUserUseCase.invoke(username, password)
                _userDataFlow.value = UserDataResult.Success(user)
                mainMenuRouter.goToMainMenu()
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            } finally {
                _signInProceedingFlow.value = false
            }
            _userDataFlow.value = null
        }
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            try {
                val user = checkUserIsAuthenticatedUseCase.invoke()
                user?.let {
                    _userDataFlow.value = UserDataResult.Success(it)
                    mainMenuRouter.goToMainMenu()
                }
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            }
        }
    }

    fun goToRegister() {
        viewModelScope.launch {
            authenticationRouter.goToRegister()
        }
    }

    sealed interface UserDataResult: Result {
        class Success(private val result: UserModel): UserDataResult, Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): UserDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}