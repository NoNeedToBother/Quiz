package ru.kpfu.itis.paramonov.questions.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.questions.api.model.Question
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel

class QuestionUiModelMapper: ModelMapper<Question, List<QuestionDataUiModel>> {
    override fun map(model: Question): List<QuestionDataUiModel> {
        return model.questions.map {
            val correct = it.answers.find { ans -> ans.correct }!!.answer
            val incorrect = it.answers.filter { ans -> !ans.correct }
            val answers = mutableListOf<AnswerDataUiModel>()
            answers.add(AnswerDataUiModel(correct, chosen = false, correct = true))
            for (incorrectAnswer in incorrect) {
                answers.add(AnswerDataUiModel(incorrectAnswer.answer, chosen = false, correct = false))
            }
            QuestionDataUiModel(
                text = it.text, answers = answers.shuffled()
            )
        }
    }
}
