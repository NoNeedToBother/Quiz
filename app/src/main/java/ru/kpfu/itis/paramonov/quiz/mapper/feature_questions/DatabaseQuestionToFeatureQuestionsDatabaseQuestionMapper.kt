package ru.kpfu.itis.paramonov.quiz.mapper.feature_questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.database.external.domain.model.QuestionModel
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseAnswer
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion
import javax.inject.Inject

class DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper @Inject constructor(): ModelMapper<QuestionModel, DatabaseQuestion> {
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