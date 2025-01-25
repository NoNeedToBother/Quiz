package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.utils.toEnumName
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.QuestionSettingsUiModel

class QuestionSettingsViewModel(
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase
): BaseViewModel() {

    private val _settingsDataFlow = MutableStateFlow<QuestionSettingsUiModel?>(null)

    val settingsDataFlow: StateFlow<QuestionSettingsUiModel?> get() = _settingsDataFlow

    fun getQuestionSettings() {
        viewModelScope.launch {
            _settingsDataFlow.value = null
            _settingsDataFlow.value = getQuestionSettingsUseCase.invoke()
        }
    }

    fun saveQuestionSettings(difficulty: String?, category: String?, gameMode: String?) {
        val difficultyToSave = difficulty?.let {
            DifficultyUiModel.valueOf(it.toEnumName())
        }
        val categoryToSave = category?.let {
            CategoryUiModel.valueOf(it.toEnumName())
        }
        val gameModeToSave = gameMode?.let {
            GameModeUiModel.valueOf(it.toEnumName())
        }

        viewModelScope.launch {
            saveQuestionSettingsUseCase.invoke(difficultyToSave, categoryToSave, gameModeToSave)
        }
    }
}