package ru.kpfu.itis.paramonov.quiz.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

val navigationModule = DI.Module("Navigation") {
    bind<Navigator>() with singleton { Navigator() }
    bind<UserRouter>() with provider {
        val navigator: Navigator = instance()
        navigator
    }
    bind<MainMenuRouter>() with provider {
        val navigator: Navigator = instance()
        navigator
    }
    bind<QuestionsRouter>() with provider {
        val navigator: Navigator = instance()
        navigator
    }
    bind<AuthenticationRouter>() with provider {
        val navigator: Navigator = instance()
        navigator
    }
}