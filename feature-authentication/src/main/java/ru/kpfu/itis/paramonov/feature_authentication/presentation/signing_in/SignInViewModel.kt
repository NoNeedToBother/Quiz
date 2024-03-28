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
import javax.inject.Inject

class SignInViewModel(
    private val authenticateUserUseCase: AuthenticateUserUseCase
): ViewModel() {

    private val _userDataFlow = MutableStateFlow<SigningInResult?>(null)

    val userDataFlow: StateFlow<SigningInResult?> get() = _userDataFlow

    fun authenticateUser(username: String, password: String) {
        _userDataFlow.value = null

        viewModelScope.launch {
            try {
                val user = authenticateUserUseCase.invoke(username, password)
                _userDataFlow.value = SigningInResult.Success(user)
            } catch (ex: Exception) {
                _userDataFlow.value = SigningInResult.Failure(ex)
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

    override fun onCleared() {
        super.onCleared()
    }
}