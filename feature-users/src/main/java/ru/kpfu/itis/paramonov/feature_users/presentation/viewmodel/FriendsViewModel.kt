package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.common_android.utils.emitException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.navigation.UserRouter

class FriendsViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val userRouter: UserRouter
): BaseViewModel() {

    private val _friendsFlow = MutableStateFlow<GetFriendsResult?>(null)

    val friendsFlow: StateFlow<GetFriendsResult?> get() = _friendsFlow

    private val _friendsPagingFlow = MutableStateFlow<GetFriendsResult?>(null)

    val friendsPagingFlow: StateFlow<GetFriendsResult?> get() = _friendsPagingFlow

    fun getFriends(max: Int) {
        viewModelScope.launch {
            try {
                val friends = getFriendsUseCase.invoke(0, max)
                _friendsFlow.value = GetFriendsResult.Success(friends)
            } catch (ex: Throwable) {
                _friendsFlow.emitException(
                    GetFriendsResult.Failure(ex)
                )
            }
        }
    }

    fun loadNextFriends(offset: Int, max: Int) {
        viewModelScope.launch {
            try {
                val users = getFriendsUseCase.invoke(offset, max)
                _friendsPagingFlow.value = GetFriendsResult.Success(users)
            } catch (ex: Throwable) {
                _friendsPagingFlow.emitException(
                    GetFriendsResult.Failure(ex)
                )
            }
        }
    }

    fun navigateToUser(id: String) {
        viewModelScope.launch {
            userRouter.goToUser(id)
        }
    }


    sealed interface GetFriendsResult: Result {
        class Success(private val result: List<UserModel>): GetFriendsResult, Result.Success<List<UserModel>> {
            override fun getValue(): List<UserModel> = result
        }
        class Failure(private val ex: Throwable): GetFriendsResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}