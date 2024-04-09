package ru.kpfu.itis.paramonov.quiz.di.modules

import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

@Module
class NavigationModule {

    @ApplicationScope
    @Provides
    fun navigator(): Navigator = Navigator()

    @ApplicationScope
    @Provides
    fun authenticationRouter(navigator: Navigator): AuthenticationRouter = navigator
}