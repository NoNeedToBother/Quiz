package ru.kpfu.itis.paramonov.core.api

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

interface CommonApi {

    fun resourceManager(): ResourceManager

    fun passwordValidator(): PasswordValidator

    fun usernameValidator(): UsernameValidator

    fun dispatcher(): CoroutineDispatcher

    fun htmlDecoder(): HtmlDecoder

    fun dateTimeParser(): DateTimeParser
}