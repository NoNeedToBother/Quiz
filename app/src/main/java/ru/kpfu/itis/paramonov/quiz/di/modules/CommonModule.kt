package ru.kpfu.itis.paramonov.quiz.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.utils.ResourceManagerImpl

@Module
class CommonModule {

    @Provides
    fun resourceManager(context: Context): ResourceManager {
        return ResourceManagerImpl(context)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}