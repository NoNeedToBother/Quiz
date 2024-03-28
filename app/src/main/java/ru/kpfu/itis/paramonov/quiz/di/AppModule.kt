package ru.kpfu.itis.paramonov.quiz.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.quiz.App

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: App): Context {
        return application
    }
}