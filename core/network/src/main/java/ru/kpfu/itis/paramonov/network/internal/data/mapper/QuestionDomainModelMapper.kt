package ru.kpfu.itis.paramonov.network.internal.data.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.network.external.domain.model.QuestionDataDomainModel
import ru.kpfu.itis.paramonov.network.external.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.network.internal.data.model.QuestionDataResponse
import ru.kpfu.itis.paramonov.network.internal.data.model.QuestionResponse
import ru.kpfu.itis.paramonov.network.internal.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.network.internal.utils.Params
import java.lang.RuntimeException

internal class QuestionDomainModelMapper(
    private val exceptionHandler: QuestionExceptionHandler,
    private val htmlDecoder: HtmlDecoder
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
                        result.add(
                            QuestionDataDomainModel(
                            htmlDecoder.decode(text),
                            htmlDecoder.decode(answer),
                            incorrectAnswers.map {
                                htmlDecoder.decode(it)
                            })
                        )
                    } else throw RuntimeException()
                }
            }
        } ?: throw RuntimeException()
        return result
    }
}