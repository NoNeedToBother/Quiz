package ru.kpfu.itis.paramonov.common.api

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.common.validators.PasswordValidator
import ru.kpfu.itis.paramonov.common.validators.UsernameValidator

interface CommonApi {

    fun resourceManager(): ResourceManager

    fun passwordValidator(): PasswordValidator

    fun usernameValidator(): UsernameValidator

    fun dispatcher(): CoroutineDispatcher

    fun htmlDecoder(): HtmlDecoder

    fun dateTimeParser(): DateTimeParser
}