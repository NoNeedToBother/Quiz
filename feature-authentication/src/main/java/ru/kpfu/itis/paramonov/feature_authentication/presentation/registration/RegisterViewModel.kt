package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
): BaseViewModel() {

    private val _userDataFlow = MutableStateFlow<RegistrationResult?>(null)

    val userDataFlow: StateFlow<RegistrationResult?> get() = _userDataFlow

    private val _registerProceedingFlow = MutableStateFlow(false)

    val registerProceedingFlow: StateFlow<Boolean> get() = _registerProceedingFlow

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        _userDataFlow.value = null
        _registerProceedingFlow.value = true

        viewModelScope.launch {
            try {
                val user = registerUserUseCase.invoke(username, email, password, confirmPassword)
                _userDataFlow.value = RegistrationResult.Success(user)
            } catch (ex: Throwable) {
                _userDataFlow.value = RegistrationResult.Failure(ex)
            } finally {
                _registerProceedingFlow.value = false
            }
        }
    }

    sealed interface RegistrationResult: Result {
        class Success(private val result: UserModel): RegistrationResult, Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): RegistrationResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}