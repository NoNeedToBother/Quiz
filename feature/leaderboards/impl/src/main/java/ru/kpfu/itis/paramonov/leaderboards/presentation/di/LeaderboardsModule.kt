package ru.kpfu.itis.paramonov.leaderboards.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetDifficultyUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsDomainModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetDifficultyUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetFriendsLeaderboardUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGameModeUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGlobalLeaderboardUseCaseImpl
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.leaderboards.presentation.viewmodel.LeaderboardsViewModel
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

    fun getGlobalLeaderboardUseCase(impl: GetGlobalLeaderboardUseCaseImpl): GetGlobalLeaderboardUseCase = impl

    fun getFriendsLeaderboardUseCase(impl: GetFriendsLeaderboardUseCaseImpl): GetFriendsLeaderboardUseCase = impl

    fun getGameModeUseCase(impl: GetGameModeUseCaseImpl): GetGameModeUseCase = impl

    fun getDifficultyUseCase(impl: GetDifficultyUseCaseImpl): GetDifficultyUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(LeaderboardsViewModel::class)
    fun provideLeaderboardViewModel(
        getGlobalLeaderboardUseCase: GetGlobalLeaderboardUseCase,
        getFriendsLeaderboardUseCase: GetFriendsLeaderboardUseCase,
        getGameModeUseCase: GetGameModeUseCase,
        getDifficultyUseCase: GetDifficultyUseCase,
        questionSettingsDomainModelMapper: QuestionSettingsDomainModelMapper,
        questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
        resultUiModelMapper: ResultUiModelMapper,
        userRouter: UserRouter
    ): ViewModel {
        return LeaderboardsViewModel(
            getGlobalLeaderboardUseCase = getGlobalLeaderboardUseCase,
            getFriendsLeaderboardUseCase = getFriendsLeaderboardUseCase,
            getGameModeUseCase = getGameModeUseCase,
            getDifficultyUseCase = getDifficultyUseCase,
            questionSettingsDomainModelMapper = questionSettingsDomainModelMapper,
            questionSettingsUiModelMapper = questionSettingsUiModelMapper,
            resultUiModelMapper = resultUiModelMapper,
            userRouter = userRouter
        )
    }
}