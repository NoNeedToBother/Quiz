package ru.kpfu.itis.paramonov.questions.presentation.settings.mvi

import ru.kpfu.itis.paramonov.questions.presentation.settings.model.QuestionSettingsUiModel
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.TrainingQuestionSettingsUiModel

data class QuestionSettingsScreenState(
    val settings: QuestionSettingsUiModel? = null,
    val trainingSettings: TrainingQuestionSettingsUiModel? = null
)
