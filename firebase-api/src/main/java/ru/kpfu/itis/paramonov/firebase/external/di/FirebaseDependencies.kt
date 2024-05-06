package ru.kpfu.itis.paramonov.firebase.external.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.common.validators.PasswordValidator
import ru.kpfu.itis.paramonov.common.validators.UsernameValidator

interface FirebaseDependencies {
    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun dateTimeParser(): DateTimeParser

    fun usernameValidator(): UsernameValidator

    fun passwordValidator(): PasswordValidator
}