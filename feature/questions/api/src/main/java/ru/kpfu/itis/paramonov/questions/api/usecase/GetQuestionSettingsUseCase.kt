package ru.kpfu.itis.paramonov.questions.api.usecase

import ru.kpfu.itis.paramonov.questions.api.model.QuestionSettings

interface GetQuestionSettingsUseCase {

    suspend operator fun invoke(): QuestionSettings

}
