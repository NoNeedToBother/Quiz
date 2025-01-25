package ru.kpfu.itis.paramonov.feature_leaderboards.di

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
                .firebaseApi(dependenciesContainer.firebaseApi())
                .localDatabaseApi(dependenciesContainer.localDatabaseApi())
                .build()
        return DaggerFeatureLeaderboardsComponent.builder()
            .dependencies(featureLeaderboardsDependencies)
            .userRouter(userRouter)
            .build()
    }
}