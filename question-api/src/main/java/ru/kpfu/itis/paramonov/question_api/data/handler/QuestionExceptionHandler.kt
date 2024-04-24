package ru.kpfu.itis.paramonov.question_api.data.handler

import retrofit2.HttpException
import ru.kpfu.itis.paramonov.common.handler.ExceptionHandler
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.question_api.R
import ru.kpfu.itis.paramonov.question_api.data.exceptions.ConnectionException
import ru.kpfu.itis.paramonov.question_api.data.exceptions.GetQuestionException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class QuestionExceptionHandler (
    private val resourceManager: ResourceManager
): ExceptionHandler {
    override fun handle(ex: Throwable): Throwable {
        return when(ex) {
            is HttpException -> {
                when(ex.code()) {
                    TOO_MANY_REQUESTS_ERROR_CODE -> GetQuestionException(
                        resourceManager.getString(R.string.too_many_requests)
                    )
                    else -> GetQuestionException(resourceManager.getString(R.string.get_questions_fail))
                }
            }
            is SocketTimeoutException -> {
                ConnectionException(resourceManager.getString(
                    R.string.connection_timeout
                ))
            }
            is UnknownHostException -> {
                ConnectionException(resourceManager.getString(
                    R.string.no_connection
                ))
            }
            else -> GetQuestionException(resourceManager.getString(R.string.get_questions_fail))
        }
    }

    fun handle(code: Int): Throwable {
        return when(code) {
            API_RATE_LIMIT_CODE -> GetQuestionException(
                resourceManager.getString(R.string.too_many_requests)
            )
            API_NO_RESULT_CODE -> GetQuestionException(
                resourceManager.getString(R.string.no_results)
            )
            else -> GetQuestionException(
                resourceManager.getString(R.string.get_questions_fail)
            )
        }
    }


    companion object {
        private const val TOO_MANY_REQUESTS_ERROR_CODE = 429

        private const val API_RATE_LIMIT_CODE = 6
        private const val API_NO_RESULT_CODE = 1
    }
}