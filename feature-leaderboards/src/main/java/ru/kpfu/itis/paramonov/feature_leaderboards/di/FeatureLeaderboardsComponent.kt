package ru.kpfu.itis.paramonov.feature_leaderboards.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di.LeaderboardsComponent
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.quiz.di.scopes.FeatureScope

@Component(
    dependencies = [
        FeatureLeaderboardsDependencies::class
    ]
)
@FeatureScope
interface FeatureLeaderboardsComponent {

    fun leaderboardComponentFactory(): LeaderboardsComponent.Factory

    @Component.Builder
    interface Builder {

        fun dependencies(dependencies: FeatureLeaderboardsDependencies): Builder

        fun userRouter(@BindsInstance userRouter: UserRouter): Builder

        fun build(): FeatureLeaderboardsComponent
    }

    @Component(
        dependencies = [
            LocalDatabaseApi::class,
            CommonApi::class,
            FirebaseApi::class
        ]
    )
    interface FeatureLeaderboardsDependenciesComponent: FeatureLeaderboardsDependencies
}