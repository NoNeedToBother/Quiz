package ru.kpfu.itis.paramonov.quiz.mapper.feature_questions

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.network.external.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.questions.api.model.AnswerData
import ru.kpfu.itis.paramonov.questions.api.model.Question
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import javax.inject.Inject

class QuestionToFeatureQuestionsQuestionMapper @Inject constructor(): ModelMapper<QuestionDomainModel, Question> {
    override fun map(model: QuestionDomainModel): Question {
        return Question(
            questions = model.questions.map { questionData ->
                val answers = mutableListOf<AnswerData>()
                answers.add(AnswerData(answer = questionData.answer, correct = true, chosen = false))
                questionData.incorrectAnswers.forEach {
                    answers.add(AnswerData(answer = it, correct = false, chosen = false))
                }

                QuestionData(
                    text = questionData.text,
                    answers = answers
                )
            }
        )
    }
}