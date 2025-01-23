package ru.kpfu.itis.paramonov.feature_leaderboards.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di.LeaderboardsComponent
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.database.external.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope

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