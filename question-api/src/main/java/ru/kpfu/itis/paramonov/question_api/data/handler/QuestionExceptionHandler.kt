package ru.kpfu.itis.paramonov.question_api.data.handler

import ru.kpfu.itis.paramonov.common.handler.ExceptionHandler
import javax.inject.Inject

class QuestionExceptionHandler @Inject constructor(): ExceptionHandler {
    override fun handle(ex: Throwable): Throwable {
        return ex
    }

    fun handle(code: Int): Throwable {
        return RuntimeException()
    }


}