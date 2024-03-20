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
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.AuthenticateUserUseCase
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
): ViewModel() {

    private val _userDataFlow = MutableStateFlow<UserModel?>(null)

    val userDataFlow: StateFlow<UserModel?> get() = _userDataFlow

    val errorsChannel = Channel<Throwable>()

    fun authenticateUser(username: String, password: String) {
        _userDataFlow.value = null

        viewModelScope.launch {
            try {
                _userDataFlow.value = authenticateUserUseCase.invoke(username, password)
                Log.i("SUCCESS", "GOOD!")
            } catch (ex: Exception) {
                errorsChannel.send(ex)
                Log.i("EXCEPTION", ex.message ?: "ERROR! ERROR! ERROR!")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        errorsChannel.close()
    }
}