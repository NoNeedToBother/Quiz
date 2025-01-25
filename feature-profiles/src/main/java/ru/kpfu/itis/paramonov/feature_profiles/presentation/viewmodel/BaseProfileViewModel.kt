package ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.ResultUiModel

abstract class BaseProfileViewModel: BaseViewModel() {
    protected val _userDataFlow = MutableStateFlow<UserDataResult?>(null)

    val userDataFlow: StateFlow<UserDataResult?> get() = _userDataFlow

    protected val _resultsDataFlow = MutableStateFlow<LastResultsDataResult?>(null)
    val resultsDataFlow: StateFlow<LastResultsDataResult?> get() = _resultsDataFlow

    sealed interface UserDataResult: Result {
        class Success(private val result: UserModel): UserDataResult, Result.Success<UserModel> {
            override fun getValue(): UserModel = result
        }
        class Failure(private val ex: Throwable): UserDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }

    sealed interface LastResultsDataResult: Result {
        class Success(private val result: List<ResultUiModel>): LastResultsDataResult,
            Result.Success<List<ResultUiModel>> {
            override fun getValue(): List<ResultUiModel> = result
        }
        class Failure(private val ex: Throwable): LastResultsDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }


}