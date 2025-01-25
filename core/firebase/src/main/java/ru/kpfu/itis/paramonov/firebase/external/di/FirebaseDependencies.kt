package ru.kpfu.itis.paramonov.firebase.external.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

interface FirebaseDependencies {
    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun dateTimeParser(): DateTimeParser

    fun usernameValidator(): UsernameValidator

    fun passwordValidator(): PasswordValidator
}