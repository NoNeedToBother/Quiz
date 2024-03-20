package ru.kpfu.itis.paramonov.common.handler

interface ExceptionHandler {
    fun handle(ex: Throwable): Throwable
}