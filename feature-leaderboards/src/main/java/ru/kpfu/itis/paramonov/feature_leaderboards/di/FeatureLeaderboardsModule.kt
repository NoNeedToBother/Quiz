package ru.kpfu.itis.paramonov.feature_leaderboards.di

import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope
import ru.kpfu.itis.paramonov.feature_leaderboards.data.datasource.ResultSource
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import javax.inject.Qualifier

@Module
class FeatureLeaderboardsModule {

    @FriendsResultSource
    @Provides
    @FeatureScope
    fun friendsResultSource(resultRepository: ResultRepository): ResultSource {
        return ResultSource(resultRepository, ResultSource.SourceType.FRIENDS)
    }

    @GlobalResultSource
    @Provides
    @FeatureScope
    fun globalResult(resultRepository: ResultRepository): ResultSource {
        return ResultSource(resultRepository, ResultSource.SourceType.GLOBAL)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FriendsResultSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GlobalResultSource