package ru.kpfu.itis.paramonov.core.handler

interface ExceptionHandler {
    fun handle(ex: Throwable): Throwable
}
