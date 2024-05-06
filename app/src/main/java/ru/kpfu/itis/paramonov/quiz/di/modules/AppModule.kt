package ru.kpfu.itis.paramonov.quiz.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.quiz.App

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun context(application: App): Context {
        return application
    }
}