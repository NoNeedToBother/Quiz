package ru.kpfu.itis.paramonov.quiz.di.modules

import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

@Module
class NavigationModule {

    @ApplicationScope
    @Provides
    fun navigator(): Navigator = Navigator()

    @Provides
    fun authenticationRouter(navigator: Navigator): AuthenticationRouter = navigator

    @Provides
    fun mainMenuRouter(navigator: Navigator): MainMenuRouter = navigator

    @Provides
    fun questionsRouter(navigator: Navigator): QuestionsRouter = navigator

    @Provides
    fun userRouter(navigator: Navigator): UserRouter = navigator
}