package ru.kpfu.itis.paramonov.users.presentation.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.core.utils.emitException
import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper

class FriendsViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val userRouter: UserRouter,
    private val userUiModelMapper: UserUiModelMapper
): BaseViewModel() {

    private val _friendsFlow = MutableStateFlow<GetFriendsResult?>(null)

    val friendsFlow: StateFlow<GetFriendsResult?> get() = _friendsFlow

    private val _friendsPagingFlow = MutableStateFlow<GetFriendsResult?>(null)

    val friendsPagingFlow: StateFlow<GetFriendsResult?> get() = _friendsPagingFlow

    fun getFriends(max: Int) {
        viewModelScope.launch {
            try {
                val friends = getFriendsUseCase.invoke(0, max).map { user -> userUiModelMapper.map(user) }
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
                val users = getFriendsUseCase.invoke(offset, max).map { user -> userUiModelMapper.map(user) }
                _friendsPagingFlow.value = GetFriendsResult.Success(users)
            } catch (ex: Throwable) {
                _friendsPagingFlow.emitException(
                    GetFriendsResult.Failure(ex)
                )
            }
        }
    }

    fun navigateToUser(id: String, sharedView: View) {
        viewModelScope.launch {
            userRouter.withSharedView(sharedView) {
                goToUser(id)
            }
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