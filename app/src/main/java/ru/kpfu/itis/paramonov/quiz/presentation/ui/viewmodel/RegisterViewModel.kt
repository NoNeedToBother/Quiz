package ru.kpfu.itis.paramonov.quiz.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.kirich1409.viewbindingdelegate.fragmentViewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.quiz.domain.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.quiz.presentation.model.UserModel
import java.lang.Exception
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
                Log.i("FAIL", ex.message ?: "ERROR! ERROR! ERROR!")
                //errorsChannel.send(ex)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        errorsChannel.close()
    }
}