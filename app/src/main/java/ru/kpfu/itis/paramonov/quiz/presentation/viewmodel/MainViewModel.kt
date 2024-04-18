package ru.kpfu.itis.paramonov.quiz.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.quiz.domain.usecase.LogoutUserUseCase

class MainViewModel(
    private val logoutUserUseCase: LogoutUserUseCase
): BaseViewModel() {

    fun logoutUser() {
        viewModelScope.launch {
            logoutUserUseCase.invoke()
        }
    }
}