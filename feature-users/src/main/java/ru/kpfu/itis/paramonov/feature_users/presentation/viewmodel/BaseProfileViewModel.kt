package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel

abstract class BaseProfileViewModel: BaseViewModel() {
    protected val _userDataFlow = MutableStateFlow<UserDataResult?>(null)

    val userDataFlow: StateFlow<UserDataResult?> get() = _userDataFlow

    sealed interface UserDataResult: Result {
        class Success(private val result: UserModel): UserDataResult, Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): UserDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }


}