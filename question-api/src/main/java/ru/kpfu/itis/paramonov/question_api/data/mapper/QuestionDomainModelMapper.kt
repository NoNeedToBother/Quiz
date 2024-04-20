package ru.kpfu.itis.paramonov.question_api.data.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.question_api.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.question_api.data.model.QuestionDataResponse
import ru.kpfu.itis.paramonov.question_api.data.model.QuestionResponse
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDataDomainModel
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.question_api.utils.Params
import java.lang.RuntimeException

class QuestionDomainModelMapper(
    private val exceptionHandler: QuestionExceptionHandler
): ModelMapper<QuestionResponse, QuestionDomainModel> {
    override fun map(model: QuestionResponse): QuestionDomainModel {
        model.code?.let {
            if (it != Params.RESPONSE_SUCCESS_CODE) throw exceptionHandler.handle(it)
            return QuestionDomainModel(mapQuestionData(model.questions))
        } ?: throw RuntimeException()
    }

    private fun mapQuestionData(questionsData: List<QuestionDataResponse>?): List<QuestionDataDomainModel> {
        val result = ArrayList<QuestionDataDomainModel>()
        questionsData?.run {
            for (questionData in questionsData) {
                with(questionData) {
                    if (text != null && answer != null && incorrectAnswers != null) {
                        result.add(QuestionDataDomainModel(text, answer, incorrectAnswers))
                    } else throw RuntimeException()
                }
            }
        } ?: throw RuntimeException()
        return result
    }
}