package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetUserUseCase

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase
): BaseProfileViewModel() {

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
}