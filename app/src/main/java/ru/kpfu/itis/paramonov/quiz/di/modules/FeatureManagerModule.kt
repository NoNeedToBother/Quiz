package ru.kpfu.itis.paramonov.quiz.di.modules

import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope

@Module
class FeatureManagerModule {

    @ApplicationScope
    @Provides
    fun featureHolderManager(featureApiHolderMap: @JvmSuppressWildcards Map<Class<*>, FeatureApiHolder>): FeatureHolderManager {
        return FeatureHolderManager(featureApiHolderMap)
    }
}