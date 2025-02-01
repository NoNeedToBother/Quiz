package ru.kpfu.itis.paramonov.questions.api.usecase

interface SaveTrainingQuestionSettingsUseCase {

    suspend operator fun invoke(limit: Int)

}