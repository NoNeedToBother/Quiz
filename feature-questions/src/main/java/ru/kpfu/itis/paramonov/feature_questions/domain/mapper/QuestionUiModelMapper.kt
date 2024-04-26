package ru.kpfu.itis.paramonov.feature_questions.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import javax.inject.Inject

class QuestionUiModelMapper @Inject constructor(): ModelMapper<QuestionDomainModel, List<QuestionDataUiModel>> {
    override fun map(model: QuestionDomainModel): List<QuestionDataUiModel> {
        return model.questions.map {
            val correct = it.answer
            val incorrect = it.incorrectAnswers
            val answers = mutableListOf<AnswerDataUiModel>()
            answers.add(AnswerDataUiModel(correct, chosen = false, correct = true))
            for (incorrectAnswer in incorrect) {
                answers.add(AnswerDataUiModel(incorrectAnswer, chosen = false, correct = false))
            }
            QuestionDataUiModel(
                text = it.text, answers = answers.shuffled()
            )
        }
    }
}