package ru.kpfu.itis.paramonov.quiz.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.common_android.utils.ResourceManagerImpl
import ru.kpfu.itis.paramonov.quiz.utils.HtmlDecoderImpl

@Module
class CommonModule {

    @Provides
    fun resourceManager(context: Context): ResourceManager {
        return ResourceManagerImpl(context)
    }

    @Provides
    fun dispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun htmlDecoder(): HtmlDecoder {
        return HtmlDecoderImpl()
    }
}