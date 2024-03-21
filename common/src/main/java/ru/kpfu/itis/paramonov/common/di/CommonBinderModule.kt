package ru.kpfu.itis.paramonov.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.resources.ResourceManagerImpl

@Module
@InstallIn(SingletonComponent::class)
interface CommonBinderModule {

    @Binds
    fun bindResManagerToImpl(
        impl: ResourceManagerImpl
    ): ResourceManager
}