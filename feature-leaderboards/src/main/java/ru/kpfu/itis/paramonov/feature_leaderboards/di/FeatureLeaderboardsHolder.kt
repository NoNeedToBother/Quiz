package ru.kpfu.itis.paramonov.feature_leaderboards.di

import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import javax.inject.Inject

@ApplicationScope
class FeatureLeaderboardsHolder @Inject constructor(
    private val dependenciesContainer: FeatureLeaderboardsDependenciesContainer
): FeatureApiHolder(dependenciesContainer){
    override fun initializeDependencies(): Any {
        val featureLeaderboardsDependencies =
            DaggerFeatureLeaderboardsComponent_FeatureLeaderboardsDependenciesComponent.builder()
                .commonApi(dependenciesContainer.commonApi())
                .firebaseApi(dependenciesContainer.firebaseApi())
                .build()
        return DaggerFeatureLeaderboardsComponent.builder()
            .dependencies(featureLeaderboardsDependencies)
            .build()
    }
}