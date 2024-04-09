package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.AuthenticateUserUseCase
import java.lang.Exception

class SignInViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase
): ViewModel() {

    private val _userDataFlow = MutableStateFlow<SigningInResult?>(null)

    val userDataFlow: StateFlow<SigningInResult?> get() = _userDataFlow

    private val _signInProceedingFlow = MutableStateFlow(false)
    val signInProceedingFlow: StateFlow<Boolean> get() = _signInProceedingFlow

    fun authenticateUser(username: String, password: String) {
        _userDataFlow.value = null
        _signInProceedingFlow.value = true

        viewModelScope.launch {
            try {
                val user = authenticateUserUseCase.invoke(username, password)
                _userDataFlow.value = SigningInResult.Success(user)
            } catch (ex: Exception) {
                _userDataFlow.value = SigningInResult.Failure(ex)
            } finally {
                _signInProceedingFlow.value = false
            }
        }
    }

    sealed class SigningInResult: BaseViewModel.Result {
        class Success(private val result: UserModel): SigningInResult(), BaseViewModel.Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): SigningInResult(), BaseViewModel.Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}