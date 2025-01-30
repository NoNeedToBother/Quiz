package ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.questions.api.usecase.GetMaxScoreUseCase
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel

class ResultViewModel(
    private val getMaxScoreUseCase: GetMaxScoreUseCase
): BaseViewModel() {

    private val _maxScoreFlow = MutableStateFlow<Double?>(null)

    val maxScoreFlow: StateFlow<Double?> get() = _maxScoreFlow

    fun getResultScore() {
        viewModelScope.launch {
            _maxScoreFlow.value = getMaxScoreUseCase.invoke()
        }
    }

}