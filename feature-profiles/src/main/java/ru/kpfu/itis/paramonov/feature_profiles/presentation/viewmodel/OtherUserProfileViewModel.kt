package ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.utils.emitException
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.FriendStatusUiModel

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getFriendStatusUseCase: GetFriendStatusUseCase,
    private val getUserLastResultsUseCase: GetUserLastResultsUseCase
): BaseProfileViewModel() {

    private val _sendFriendRequestResultFlow = MutableStateFlow<Boolean?>(null)

    val sendFriendRequestResultFlow: StateFlow<Boolean?> get() = _sendFriendRequestResultFlow

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
                _sendFriendRequestResultFlow.value = true
            } catch (ex: Throwable) {
                _sendFriendRequestResultFlow.value = false
            } finally {
                _sendFriendRequestResultFlow.value = null
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

    fun getLastResults(max: Int, id: String) {
        viewModelScope.launch {
            try {
                val results = getUserLastResultsUseCase.invoke(max, id)
                _resultsDataFlow.value = LastResultsDataResult.Success(results)
            } catch (ex: Throwable) {
                _resultsDataFlow.emitException(LastResultsDataResult.Failure(ex))
            } finally {
                _resultsDataFlow.value = null
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