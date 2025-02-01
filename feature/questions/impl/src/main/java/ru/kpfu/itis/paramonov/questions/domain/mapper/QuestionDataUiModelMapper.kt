package ru.kpfu.itis.paramonov.questions.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import javax.inject.Inject

class QuestionDataUiModelMapper @Inject constructor(): ModelMapper<QuestionData, QuestionDataUiModel> {
    override fun map(model: QuestionData): QuestionDataUiModel {
        return QuestionDataUiModel(
            text = model.text,
            answers = model.answers.map { ans ->
                AnswerDataUiModel(
                    answer = ans.answer,
                    chosen = ans.chosen,
                    correct = ans.correct
                )
            }
        )
    }
}
