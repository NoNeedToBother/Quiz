package ru.kpfu.itis.paramonov.questions.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.toEnumName
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.QuestionSettingsUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.TrainingQuestionSettingsUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.mvi.QuestionSettingsScreenSideEffect
import ru.kpfu.itis.paramonov.questions.presentation.settings.mvi.QuestionSettingsScreenState

class QuestionSettingsViewModel(
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase,
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
    private val questionSettingsApiModelMapper: QuestionSettingsApiModelMapper,
    private val saveTrainingQuestionSettingsUseCase: SaveTrainingQuestionSettingsUseCase,
    private val getTrainingQuestionSettingsUseCase: GetTrainingQuestionSettingsUseCase,
    private val resourceManager: ResourceManager
): ViewModel(), ContainerHost<QuestionSettingsScreenState, QuestionSettingsScreenSideEffect> {

    override val container = container<QuestionSettingsScreenState, QuestionSettingsScreenSideEffect>(
        QuestionSettingsScreenState()
    )

    fun getQuestionSettings() = intent {
        val questionSettings = getQuestionSettingsUseCase.invoke()
        val questionSettingsUiModel = QuestionSettingsUiModel(
            difficulty = questionSettingsUiModelMapper.mapDifficulty(questionSettings.difficulty),
            category = questionSettingsUiModelMapper.mapCategory(questionSettings.category),
            gameMode = questionSettingsUiModelMapper.mapGameMode(questionSettings.gameMode)
        )
        reduce { state.copy(settings = questionSettingsUiModel) }
    }

    fun saveQuestionSettings() = intent {
        saveQuestionSettingsUseCase.invoke(
            difficulty = state.settings?.difficulty?.let { questionSettingsApiModelMapper.mapDifficulty(it) },
            category = state.settings?.category?.let { questionSettingsApiModelMapper.mapCategory(it) },
            gameMode = state.settings?.gameMode?.let { questionSettingsApiModelMapper.mapGameMode(it) }
        )
    }

    fun updateCategory(category: String) = intent {
        val newSettings = QuestionSettingsUiModel(
            difficulty = state.settings?.difficulty ?: DifficultyUiModel.EASY,
            category = CategoryUiModel.valueOf(category.toEnumName()),
            gameMode = state.settings?.gameMode ?: GameModeUiModel.BLITZ
        )
        reduce { state.copy(settings = newSettings) }
    }

    fun updateDifficulty(difficulty: String) = intent {
        val newSettings = QuestionSettingsUiModel(
            difficulty = DifficultyUiModel.valueOf(difficulty.toEnumName()),
            category = state.settings?.category ?: CategoryUiModel.GENERAL,
            gameMode = state.settings?.gameMode ?: GameModeUiModel.BLITZ
        )
        reduce { state.copy(settings = newSettings) }
    }

    fun updateGameMode(gameMode: String) = intent {
        val newSettings = QuestionSettingsUiModel(
            difficulty = state.settings?.difficulty ?: DifficultyUiModel.EASY,
            category = state.settings?.category ?: CategoryUiModel.GENERAL,
            gameMode = GameModeUiModel.valueOf(gameMode.toEnumName())
        )
        reduce { state.copy(settings = newSettings) }
    }

    fun updateLimit(limit: Int) = intent {
        val newTrainingSettings = TrainingQuestionSettingsUiModel(
            limit = limit
        )
        reduce { state.copy(trainingSettings = newTrainingSettings) }
    }

    fun getTrainingQuestionSettings() = intent {
        val settings = TrainingQuestionSettingsUiModel(
            limit = getTrainingQuestionSettingsUseCase.invoke().limit
        )
        reduce { state.copy(trainingSettings = settings) }
    }

    fun saveTrainingQuestionSettings() = intent {
        try {
            saveTrainingQuestionSettingsUseCase.invoke(state.trainingSettings?.limit
                ?: DEFAULT_SAVE_LIMIT_VALUE)
        } catch (ex: Throwable) {
            postSideEffect(QuestionSettingsScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.save_settings_fail),
                message = resourceManager.getString(
                    R.string.limit_not_correct, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND
                )
            ))
        }
    }

    fun checkLimit(limit: Int?): String? {
        return when(limit) {
            null -> resourceManager.getString(R.string.empty_limit)
            in LIMIT_LOWER_BOUND..LIMIT_UPPER_BOUND -> null
            else -> resourceManager.getString(
                R.string.limit_not_correct, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND
            )
        }
    }

    companion object {
        private const val LIMIT_LOWER_BOUND = 1
        private const val LIMIT_UPPER_BOUND = 100

        private const val DEFAULT_SAVE_LIMIT_VALUE = 50
    }
}
