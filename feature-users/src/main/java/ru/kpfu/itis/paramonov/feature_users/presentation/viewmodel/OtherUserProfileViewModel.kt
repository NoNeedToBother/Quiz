package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SendFriendRequestUseCase

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase
): BaseProfileViewModel() {

    private val _sendFriendRequestErrorFlow = MutableStateFlow<Throwable?>(null)

    val sendFriendRequestErrorFlow: StateFlow<Throwable?> get() = _sendFriendRequestErrorFlow

    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val user = getUserUseCase.invoke(id)
                _userDataFlow.value = UserDataResult.Success(user)
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            }
        }
    }

    fun sendFriendRequest(id: String) {
        viewModelScope.launch {
            try {
                sendFriendRequestUseCase.invoke(id)
            } catch (ex: Throwable) {
                _sendFriendRequestErrorFlow.value = ex
                _sendFriendRequestErrorFlow.value = null
            }
        }
    }
}