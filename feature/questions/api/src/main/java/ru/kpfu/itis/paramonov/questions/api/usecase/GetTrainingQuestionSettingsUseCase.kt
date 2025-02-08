package ru.kpfu.itis.paramonov.questions.api.usecase

import ru.kpfu.itis.paramonov.questions.api.model.TrainingQuestionSettings

interface GetTrainingQuestionSettingsUseCase {

    suspend operator fun invoke(): TrainingQuestionSettings

}
