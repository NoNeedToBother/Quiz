package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.TrainingQuestionSettingsUiModel

class TrainingQuestionSettingsViewModel(
    private val saveTrainingQuestionSettingsUseCase: SaveTrainingQuestionSettingsUseCase,
    private val getTrainingQuestionSettingsUseCase: GetTrainingQuestionSettingsUseCase
): BaseViewModel() {

    private val _settingsDataFlow = MutableStateFlow<TrainingQuestionSettingsResult?>(null)

    val settingsDataFlow: StateFlow<TrainingQuestionSettingsResult?> get() = _settingsDataFlow

    fun getTrainingQuestionSettings() {
        viewModelScope.launch {
            _settingsDataFlow.value = null
            _settingsDataFlow.value = TrainingQuestionSettingsResult.Success(
                    getTrainingQuestionSettingsUseCase.invoke()
                )
        }
    }

    fun saveTrainingQuestionSettings(limit: Int) {
        viewModelScope.launch {
            try {
                saveTrainingQuestionSettingsUseCase.invoke(limit)
            } catch (ex: Throwable) {
                _settingsDataFlow.value = TrainingQuestionSettingsResult.Failure(ex)
            }
        }
    }

    sealed interface TrainingQuestionSettingsResult: Result {
        class Success(private val result: TrainingQuestionSettingsUiModel): TrainingQuestionSettingsResult,
                Result.Success<TrainingQuestionSettingsUiModel> {
            override fun getValue(): TrainingQuestionSettingsUiModel = result
        }
        class Failure(private val ex: Throwable): TrainingQuestionSettingsResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}