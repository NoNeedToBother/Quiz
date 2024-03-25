package ru.kpfu.itis.paramonov.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.resources.ResourceManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    abstract fun bindResManagerToImpl(
        impl: ResourceManagerImpl
    ): ResourceManager
}