package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
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
    fun globalLeaderboardViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): LeaderboardsViewModel {
        return ViewModelProvider(fragment, factory)[LeaderboardsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(LeaderboardsViewModel::class)
    fun provideGlobalLeaderboardViewModel(
        getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
        getGameModeUseCase: GetGameModeUseCase,
        userRouter: UserRouter
    ): ViewModel {
        return LeaderboardsViewModel(
            getGlobalLeaderboardUseCase = getGlobalLeaderboardUseCase,
            getGameModeUseCase = getGameModeUseCase,
            userRouter = userRouter
        )
    }
}