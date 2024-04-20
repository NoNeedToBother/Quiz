package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.AuthenticateUserUseCase

class SignInViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase
): BaseViewModel() {

    private val _userDataFlow = MutableStateFlow<SigningInResult?>(null)

    val userDataFlow: StateFlow<SigningInResult?> get() = _userDataFlow

    private val _signInProceedingFlow = MutableStateFlow(false)
    val signInProceedingFlow: StateFlow<Boolean> get() = _signInProceedingFlow

    fun authenticateUser(username: String, password: String) {

        viewModelScope.launch {
            _userDataFlow.value = null
            _signInProceedingFlow.value = true
            try {
                val user = authenticateUserUseCase.invoke(username, password)
                _userDataFlow.value = SigningInResult.Success(user)
            } catch (ex: Throwable) {
                _userDataFlow.value = SigningInResult.Failure(ex)
            } finally {
                _signInProceedingFlow.value = false
            }
            _userDataFlow.value = null
        }
    }

    sealed interface SigningInResult: Result {
        class Success(private val result: UserModel): SigningInResult, Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): SigningInResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}