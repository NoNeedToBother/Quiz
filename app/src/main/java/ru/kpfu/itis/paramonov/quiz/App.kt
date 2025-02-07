package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton
import ru.kpfu.itis.paramonov.authentication.di.featureAuthenticationModule
import ru.kpfu.itis.paramonov.database.external.di.localDatabaseModule
import ru.kpfu.itis.paramonov.firebase.external.di.firebaseModule
import ru.kpfu.itis.paramonov.leaderboards.di.featureLeaderboardsModule
import ru.kpfu.itis.paramonov.network.external.di.questionsModule
import ru.kpfu.itis.paramonov.profiles.di.featureProfilesModule
import ru.kpfu.itis.paramonov.questions.di.featureQuestionsModule
import ru.kpfu.itis.paramonov.quiz.di.commonModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureAuthenticationAdapterModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureLeaderboardsAdapterModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureMapperAdapterModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureProfilesAdapterModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureQuestionsAdapterModule
import ru.kpfu.itis.paramonov.quiz.di.adapters.featureUsersAdapterModule
import ru.kpfu.itis.paramonov.users.di.featureUsersModule

class App: Application(), DIAware {

    override val di: DI by DI.lazy {
        bind<Context>() with singleton { applicationContext }
        import(commonModule)
        extend(firebaseModule)
        extend(questionsModule)
        import(localDatabaseModule)

        import(featureMapperAdapterModule)

        import(featureAuthenticationAdapterModule)
        extend(featureAuthenticationModule)

        extend(featureLeaderboardsModule)
        import(featureLeaderboardsAdapterModule)

        extend(featureProfilesModule)
        import(featureProfilesAdapterModule)

        extend(featureUsersModule)
        import(featureUsersAdapterModule)

        extend(featureQuestionsModule)
        import(featureQuestionsAdapterModule)
    }
}