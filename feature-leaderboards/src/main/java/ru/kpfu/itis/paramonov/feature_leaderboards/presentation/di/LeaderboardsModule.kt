package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetDifficultyUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.domain.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.LeaderboardsViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class LeaderboardsModule {

    @Provides
    fun leaderboardViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): LeaderboardsViewModel {
        return ViewModelProvider(fragment, factory)[LeaderboardsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(LeaderboardsViewModel::class)
    fun provideLeaderboardViewModel(
        getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
        getFriendsLeaderboardUseCase: GetFriendsLeaderboardUseCase,
        getGameModeUseCase: GetGameModeUseCase,
        getDifficultyUseCase: GetDifficultyUseCase,
        userRouter: UserRouter
    ): ViewModel {
        return LeaderboardsViewModel(
            getGlobalLeaderboardUseCase = getGlobalLeaderboardUseCase,
            getFriendsLeaderboardUseCase = getFriendsLeaderboardUseCase,
            getGameModeUseCase = getGameModeUseCase,
            getDifficultyUseCase = getDifficultyUseCase,
            userRouter = userRouter
        )
    }
}