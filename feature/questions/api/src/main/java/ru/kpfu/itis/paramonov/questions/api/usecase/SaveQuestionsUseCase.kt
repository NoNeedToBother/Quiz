package ru.kpfu.itis.paramonov.questions.api.usecase

import ru.kpfu.itis.paramonov.questions.api.model.QuestionData

interface SaveQuestionsUseCase {

    suspend operator fun invoke(questions: List<QuestionData>)

}