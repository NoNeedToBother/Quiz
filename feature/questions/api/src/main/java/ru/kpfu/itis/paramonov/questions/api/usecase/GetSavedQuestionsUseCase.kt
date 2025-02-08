package ru.kpfu.itis.paramonov.questions.api.usecase

import ru.kpfu.itis.paramonov.questions.api.model.QuestionData

interface GetSavedQuestionsUseCase {

    suspend operator fun invoke(): List<QuestionData>

}
