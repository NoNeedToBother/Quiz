package ru.kpfu.itis.paramonov.quiz.mapper.questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.database.external.model.AnswerModel
import ru.kpfu.itis.paramonov.database.external.model.QuestionModel
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion

class FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
    : ModelMapper<DatabaseQuestion, QuestionModel> {
    override fun map(model: DatabaseQuestion): QuestionModel {
        return QuestionModel(
            text = model.text,
            difficulty = model.difficulty,
            category = model.category,
            answers = model.answers.map { ans ->
                AnswerModel(text = ans.text, isCorrect = ans.isCorrect)
            }
        )
    }
}
