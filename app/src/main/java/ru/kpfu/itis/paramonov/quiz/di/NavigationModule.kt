package ru.kpfu.itis.paramonov.quiz.di

import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import javax.inject.Singleton

@Module
class NavigationModule {

    @ApplicationScope
    @Provides
    fun provideNavigator(): Navigator = Navigator()
}