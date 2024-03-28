package ru.kpfu.itis.paramonov.quiz.di

import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import javax.inject.Singleton

@Module
class FeatureManagerModule {

    @ApplicationScope
    @Provides
    fun provideFeatureHolderManager(featureApiHolderMap: @JvmSuppressWildcards Map<Class<*>, FeatureApiHolder>): FeatureHolderManager {
        return FeatureHolderManager(featureApiHolderMap)
    }
}