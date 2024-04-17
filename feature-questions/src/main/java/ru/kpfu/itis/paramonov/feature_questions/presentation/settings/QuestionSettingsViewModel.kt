package ru.kpfu.itis.paramonov.feature_questions.presentation.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.utils.toEnumName
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.QuestionSettingsUiModel

class QuestionSettingsViewModel(
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase
): BaseViewModel() {

    private val _settingsDataFlow = MutableStateFlow<QuestionSettingsUiModel?>(null)

    val settingsDataFlow: StateFlow<QuestionSettingsUiModel?> get() = _settingsDataFlow

    fun getQuestionSettings() {
        _settingsDataFlow.value = null

        viewModelScope.launch {
            _settingsDataFlow.value = getQuestionSettingsUseCase.invoke()
        }
    }

    fun saveQuestionSettings(difficulty: String?, category: String?, gameMode: String?) {
        val difficultyToSave = difficulty?.toEnumName() ?: difficulty
        val categoryToSave = category?.toEnumName() ?: category
        val gameModeToSave = gameMode?.toEnumName() ?: gameMode

        viewModelScope.launch {
            saveQuestionSettingsUseCase.invoke(difficultyToSave, categoryToSave, gameModeToSave)
        }
    }
}