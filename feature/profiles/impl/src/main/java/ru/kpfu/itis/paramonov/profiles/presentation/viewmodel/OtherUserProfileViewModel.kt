package ru.kpfu.itis.paramonov.profiles.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.utils.emitException
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.domain.mapper.FriendStatusUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.presentation.model.FriendStatusUiModel

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getFriendStatusUseCase: GetFriendStatusUseCase,
    private val getUserLastResultsUseCase: GetUserLastResultsUseCase,
    private val userUiModelMapper: UserUiModelMapper,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val friendStatusUiModelMapper: FriendStatusUiModelMapper
): BaseProfileViewModel() {

    private val _sendFriendRequestResultFlow = MutableStateFlow<Boolean?>(null)

    val sendFriendRequestResultFlow: StateFlow<Boolean?> get() = _sendFriendRequestResultFlow

    private val _friendStatusDataFlow = MutableStateFlow<FriendStatusDataResult?>(null)

    val friendStatusDataFlow: StateFlow<FriendStatusDataResult?> get() = _friendStatusDataFlow

    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val user = userUiModelMapper.map(getUserUseCase.invoke(id))
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
                val friendStatus = friendStatusUiModelMapper.map(
                    getFriendStatusUseCase.invoke(id)
                )
                _friendStatusDataFlow.value = FriendStatusDataResult.Success(friendStatus)
            } catch (ex: Throwable) {
                _friendStatusDataFlow.emitException(FriendStatusDataResult.Failure(ex))
            }
        }
    }

    fun getLastResults(max: Int, id: String) {
        viewModelScope.launch {
            try {
                val results = getUserLastResultsUseCase.invoke(max, id).map { res -> resultUiModelMapper.map(res) }
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