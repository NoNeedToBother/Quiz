package ru.kpfu.itis.paramonov.common.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.resources.ResourceManagerImpl

@Module
class CommonModule {

    @Provides
    fun bindResManagerToImpl(
        impl: ResourceManagerImpl
    ): ResourceManager = impl

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}