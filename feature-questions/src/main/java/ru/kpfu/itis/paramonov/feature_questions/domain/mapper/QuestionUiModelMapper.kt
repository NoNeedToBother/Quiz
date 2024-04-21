package ru.kpfu.itis.paramonov.feature_questions.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDataDomainModel
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import javax.inject.Inject

class QuestionUiModelMapper @Inject constructor(): ModelMapper<QuestionDomainModel, QuestionUiModel> {
    override fun map(model: QuestionDomainModel): QuestionUiModel {
        return QuestionUiModel(
            questions = model.questions.map {
                mapQuestionData(it)
            }
        )
    }

    private fun mapQuestionData(questionDataDomainModel: QuestionDataDomainModel): QuestionDataUiModel {
        return QuestionDataUiModel(
            text = questionDataDomainModel.text,
            answer = questionDataDomainModel.answer,
            incorrectAnswers = questionDataDomainModel.incorrectAnswers
        )
    }
}