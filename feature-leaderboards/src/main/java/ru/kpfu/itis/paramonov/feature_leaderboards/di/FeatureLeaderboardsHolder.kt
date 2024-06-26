package ru.kpfu.itis.paramonov.feature_leaderboards.di

import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
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