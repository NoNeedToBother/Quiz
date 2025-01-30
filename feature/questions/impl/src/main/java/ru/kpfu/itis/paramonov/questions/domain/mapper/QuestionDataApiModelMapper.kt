package ru.kpfu.itis.paramonov.questions.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.questions.api.model.AnswerData
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import javax.inject.Inject

class QuestionDataApiModelMapper @Inject constructor(): ModelMapper<QuestionDataUiModel, QuestionData> {
    override fun map(model: QuestionDataUiModel): QuestionData {
        return QuestionData(
            text = model.text,
            answers = model.answers.map { ans ->
                AnswerData(
                    answer = ans.answer,
                    chosen = ans.chosen,
                    correct = ans.correct
                )
            }
        )
    }
}