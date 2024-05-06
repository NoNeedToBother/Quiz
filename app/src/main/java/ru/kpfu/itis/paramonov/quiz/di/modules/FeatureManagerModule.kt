package ru.kpfu.itis.paramonov.quiz.di.modules

import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope

@Module
class FeatureManagerModule {

    @ApplicationScope
    @Provides
    fun featureHolderManager(featureApiHolderMap: @JvmSuppressWildcards Map<Class<*>, FeatureApiHolder>): FeatureHolderManager {
        return FeatureHolderManager(featureApiHolderMap)
    }
}