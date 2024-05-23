package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.common_android.utils.emitException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SearchUsersUseCase
import ru.kpfu.itis.paramonov.navigation.UserRouter

class SearchUsersViewModel(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val userRouter: UserRouter
): BaseViewModel() {

    private val _searchUsersFlow = MutableStateFlow<SearchUsersResult?>(null)

    val searchUsersFLow: StateFlow<SearchUsersResult?> get() = _searchUsersFlow

    fun searchUsers(username: String) {
        viewModelScope.launch {
            try {
                val users = searchUsersUseCase.invoke(username)
                _searchUsersFlow.value = SearchUsersResult.Success(users)
            } catch (ex: Throwable) {
                _searchUsersFlow.emitException(
                    SearchUsersResult.Failure(ex)
                )
            }
        }
    }

    fun navigateToUser(id: String) {
        viewModelScope.launch {
            userRouter.goToUser(id)
        }
    }

    sealed interface SearchUsersResult: Result {
        class Success(private val result: List<UserModel>): SearchUsersResult, Result.Success<List<UserModel>> {
            override fun getValue(): List<UserModel> = result
        }
        class Failure(private val ex: Throwable): SearchUsersResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}