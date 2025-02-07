package ru.kpfu.itis.paramonov.quiz.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.quiz.utils.DateTimeParserImpl
import ru.kpfu.itis.paramonov.quiz.utils.HtmlDecoderImpl
import ru.kpfu.itis.paramonov.quiz.utils.ResourceManagerImpl

val commonModule = DI.Module("Common") {
    bind<ResourceManager>() with provider { ResourceManagerImpl(context = instance()) }
    bind<CoroutineDispatcher>() with provider { Dispatchers.IO }
    bind<HtmlDecoder>() with provider { HtmlDecoderImpl() }
    bind<DateTimeParser>() with provider { DateTimeParserImpl() }
    bind<UsernameValidator>() with provider { UsernameValidator(resourceManager = instance()) }
    bind<PasswordValidator>() with provider { PasswordValidator(resourceManager = instance()) }
}
