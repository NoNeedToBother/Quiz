package ru.kpfu.itis.paramonov.leaderboards.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.leaderboards.presentation.di.LeaderboardsComponent
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository

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
            QuestionSettingsRepository::class,
            CommonApi::class,
            ResultRepository::class
        ]
    )
    interface FeatureLeaderboardsDependenciesComponent: FeatureLeaderboardsDependencies
}