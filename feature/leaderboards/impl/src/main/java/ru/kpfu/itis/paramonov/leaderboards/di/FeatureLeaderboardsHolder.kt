package ru.kpfu.itis.paramonov.leaderboards.di

import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.UserRouter
import javax.inject.Inject

@ApplicationScope
class FeatureLeaderboardsHolder @Inject constructor(
    private val dependenciesContainer: FeatureLeaderboardsDependenciesContainer,
    private val userRouter: UserRouter
): FeatureApiHolder(dependenciesContainer){
    override fun initializeDependencies(): Any {
        val featureLeaderboardsDependencies =
            DaggerFeatureLeaderboardsComponent_FeatureLeaderboardsDependenciesComponent.builder()
                .commonApi(dependenciesContainer.commonApi())
                .resultRepositpry(dependenciesContainer.resultRepository())
                .questionSettingsRepository(dependenciesContainer.questionSettingsRepository())
                .build()
        return DaggerFeatureLeaderboardsComponent.builder()
            .dependencies(featureLeaderboardsDependencies)
            .userRouter(userRouter)
            .build()
    }
}