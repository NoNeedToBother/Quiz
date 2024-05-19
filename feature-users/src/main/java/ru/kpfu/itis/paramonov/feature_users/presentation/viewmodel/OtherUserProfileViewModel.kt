package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.utils.emitException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.model.FriendStatusUiModel

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getFriendStatusUseCase: GetFriendStatusUseCase
): BaseProfileViewModel() {

    private val _sendFriendRequestErrorFlow = MutableStateFlow<Throwable?>(null)

    val sendFriendRequestErrorFlow: StateFlow<Throwable?> get() = _sendFriendRequestErrorFlow

    private val _friendStatusDataFlow = MutableStateFlow<FriendStatusDataResult?>(null)

    val friendStatusDataFlow: StateFlow<FriendStatusDataResult?> get() = _friendStatusDataFlow

    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val user = getUserUseCase.invoke(id)
                _userDataFlow.value = UserDataResult.Success(user)
            } catch (ex: Throwable) {
                _userDataFlow.emitException(UserDataResult.Failure(ex))
            }
        }
    }

    fun sendFriendRequest(id: String) {
        viewModelScope.launch {
            try {
                sendFriendRequestUseCase.invoke(id)
            } catch (ex: Throwable) {
                _sendFriendRequestErrorFlow.emitException(ex)
            }
        }
    }

    fun checkFriendStatus(id: String) {
        viewModelScope.launch {
            try {
                val friendStatus = getFriendStatusUseCase.invoke(id)
                _friendStatusDataFlow.value = FriendStatusDataResult.Success(friendStatus)
            } catch (ex: Throwable) {
                _friendStatusDataFlow.emitException(FriendStatusDataResult.Failure(ex))
            }
        }
    }

    sealed interface FriendStatusDataResult: Result {
        class Success(private val result: FriendStatusUiModel): Result.Success<FriendStatusUiModel>, FriendStatusDataResult {
            override fun getValue(): FriendStatusUiModel = result
        }
        class Failure(private val ex: Throwable): Result.Failure, FriendStatusDataResult {
            override fun getException(): Throwable = ex
        }
    }
}