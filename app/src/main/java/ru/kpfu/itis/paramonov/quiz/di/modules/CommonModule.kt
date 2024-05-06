package ru.kpfu.itis.paramonov.quiz.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.quiz.utils.DateTimeParserImpl
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.quiz.utils.ResourceManagerImpl
import ru.kpfu.itis.paramonov.quiz.utils.HtmlDecoderImpl

@Module
class CommonModule {

    @Provides
    fun resourceManagerImpl(context: Context): ResourceManagerImpl = ResourceManagerImpl(context)

    @Provides
    fun resourceManager(impl: ResourceManagerImpl): ResourceManager = impl

    @Provides
    fun dispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun htmlDecoderImpl(): HtmlDecoderImpl = HtmlDecoderImpl()

    @Provides
    fun htmlDecoder(impl: HtmlDecoderImpl): HtmlDecoder = impl

    @Provides
    fun dateTimeParserImpl(): DateTimeParserImpl = DateTimeParserImpl()

    @Provides
    fun dateTimeParser(impl: DateTimeParserImpl): DateTimeParser = impl
}