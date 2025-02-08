package ru.kpfu.itis.paramonov.quiz.mapper.questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.database.external.model.QuestionModel
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseAnswer
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion

class DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
    : ModelMapper<QuestionModel, DatabaseQuestion> {
    override fun map(model: QuestionModel): DatabaseQuestion {
        return DatabaseQuestion(
            text = model.text,
            difficulty = model.difficulty,
            category = model.category,
            answers = model.answers.map { ans ->
                DatabaseAnswer(text = ans.text, isCorrect = ans.isCorrect)
            }
        )
    }
}
