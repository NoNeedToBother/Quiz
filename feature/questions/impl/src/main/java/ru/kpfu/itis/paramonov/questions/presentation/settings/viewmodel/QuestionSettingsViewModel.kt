package ru.kpfu.itis.paramonov.questions.presentation.settings.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.utils.toEnumName
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.QuestionSettingsUiModel

class QuestionSettingsViewModel(
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
    private val questionSettingsApiModelMapper: QuestionSettingsApiModelMapper
): BaseViewModel() {

    private val _settingsDataFlow = MutableStateFlow<QuestionSettingsUiModel?>(null)

    val settingsDataFlow: StateFlow<QuestionSettingsUiModel?> get() = _settingsDataFlow

    fun getQuestionSettings() {
        viewModelScope.launch {
            _settingsDataFlow.value = null
            val questionSettings = getQuestionSettingsUseCase.invoke()
            _settingsDataFlow.value = QuestionSettingsUiModel(
                difficulty = questionSettingsUiModelMapper.mapDifficulty(questionSettings.difficulty),
                category = questionSettingsUiModelMapper.mapCategory(questionSettings.category),
                gameMode = questionSettingsUiModelMapper.mapGameMode(questionSettings.gameMode)
            )
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
            saveQuestionSettingsUseCase.invoke(
                difficulty = difficultyToSave?.let { questionSettingsApiModelMapper.mapDifficulty(it) },
                category = categoryToSave?.let { questionSettingsApiModelMapper.mapCategory(it) },
                gameMode = gameModeToSave?.let { questionSettingsApiModelMapper.mapGameMode(it) }
            )
        }
    }
}