package ru.kpfu.itis.paramonov.leaderboards.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetDifficultyUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetFriendsLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGameModeUseCase
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetGlobalLeaderboardUseCase
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsDomainModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetDifficultyUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetFriendsLeaderboardUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGameModeUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGlobalLeaderboardUseCaseImpl
import ru.kpfu.itis.paramonov.leaderboards.presentation.viewmodel.LeaderboardsViewModel

val featureLeaderboardsModule = DI {
    bind<GetDifficultyUseCase>() with provider {
        GetDifficultyUseCaseImpl(instance(), instance())
    }
    bind<GetFriendsLeaderboardUseCase>() with provider {
        GetFriendsLeaderboardUseCaseImpl(instance(), instance())
    }
    bind<GetGameModeUseCase>() with provider {
        GetGameModeUseCaseImpl(instance(), instance())
    }
    bind<GetGlobalLeaderboardUseCase>() with provider {
        GetGlobalLeaderboardUseCaseImpl(instance(), instance())
    }

    bind<QuestionSettingsDomainModelMapper>() with provider { QuestionSettingsDomainModelMapper() }
    bind<QuestionSettingsUiModelMapper>() with provider { QuestionSettingsUiModelMapper() }
    bind<UserUiModelMapper>() with provider { UserUiModelMapper() }
    bind<ResultUiModelMapper>() with provider {
        ResultUiModelMapper(
            questionSettingsUiModelMapper = instance(),
            userUiModelMapper = instance()
        )
    }

    bindProvider {
        LeaderboardsViewModel(
            getGlobalLeaderboardUseCase = instance(),
            getFriendsLeaderboardUseCase = instance(),
            getGameModeUseCase = instance(),
            getDifficultyUseCase = instance(),
            questionSettingsDomainModelMapper = instance(),
            resultUiModelMapper = instance(),
            questionSettingsUiModelMapper = instance(),
            resourceManager = instance()
        )
    }
}