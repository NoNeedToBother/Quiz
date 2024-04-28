package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val authenticationRouter: AuthenticationRouter,
    private val mainMenuRouter: MainMenuRouter
): BaseViewModel() {

    private val _userDataFlow = MutableStateFlow<UserDataResult?>(null)

    val userDataFlow: StateFlow<UserDataResult?> get() = _userDataFlow

    private val _registerProceedingFlow = MutableStateFlow(false)

    val registerProceedingFlow: StateFlow<Boolean> get() = _registerProceedingFlow

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerProceedingFlow.value = true
            try {
                val user = registerUserUseCase.invoke(username, email, password, confirmPassword)
                _userDataFlow.value = UserDataResult.Success(user)
                mainMenuRouter.goToMainMenu()
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            } finally {
                _registerProceedingFlow.value = false
            }
            _userDataFlow.value = null
        }
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase.invoke()
                if (user.isPresent) {
                    _userDataFlow.value = UserDataResult.Success(user.get())
                    mainMenuRouter.goToMainMenu()
                }
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            }
        }
    }

    fun goToSignIn() {
        viewModelScope.launch {
            authenticationRouter.goToSignIn()
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