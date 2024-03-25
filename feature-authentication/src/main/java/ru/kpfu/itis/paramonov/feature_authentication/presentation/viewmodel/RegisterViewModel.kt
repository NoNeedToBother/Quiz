package ru.kpfu.itis.paramonov.feature_authentication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): BaseViewModel() {

    private val _userDataFlow = MutableStateFlow<RegistrationResult?>(null)

    val userDataFlow: StateFlow<RegistrationResult?> get() = _userDataFlow

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        _userDataFlow.value = null

        viewModelScope.launch {
            try {
                val user = registerUserUseCase.invoke(username, email, password, confirmPassword)
                _userDataFlow.value = RegistrationResult.Success(user)

            } catch (ex: Throwable) {
                _userDataFlow.value = RegistrationResult.Failure(ex)
            }
        }
    }

    sealed class RegistrationResult: Result {
        class Success(private val result: UserModel): RegistrationResult(), Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): RegistrationResult(), Result.Failure {
            override fun getException(): Throwable = ex
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}