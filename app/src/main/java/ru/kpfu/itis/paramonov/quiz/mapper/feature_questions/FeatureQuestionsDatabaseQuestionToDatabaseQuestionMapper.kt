package ru.kpfu.itis.paramonov.quiz.mapper.feature_questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.database.external.domain.model.AnswerModel
import ru.kpfu.itis.paramonov.database.external.domain.model.QuestionModel
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion
import javax.inject.Inject

class FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper @Inject constructor(): ModelMapper<DatabaseQuestion, QuestionModel> {
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