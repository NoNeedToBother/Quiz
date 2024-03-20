package ru.kpfu.itis.paramonov.feature_authentication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel() {

    private val _userDataFlow = MutableStateFlow<UserModel?>(null)

    val userDataFlow: StateFlow<UserModel?> get() = _userDataFlow

    val errorsChannel = Channel<Throwable>()

    fun registerUser(username: String, email: String, password: String) {
        _userDataFlow.value = null

        viewModelScope.launch {
            try {
                _userDataFlow.value = registerUserUseCase.invoke(username, email, password)

                Log.i("SUCCESS", "GOOD!")
            } catch (ex: Throwable) {
                errorsChannel.send(ex)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        errorsChannel.close()
    }
}