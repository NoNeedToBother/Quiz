package ru.kpfu.itis.paramonov.profiles.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.profiles.domain.mapper.FriendStatusUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserLastResultsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetUserLastResultsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.LogoutUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.SubscribeToProfileUpdatesUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.AcceptFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.DenyFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendRequestsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendStatusUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.SendFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.ChangeCredentialsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.ConfirmCredentialsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.SaveProfilePictureUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.SaveUserSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.OtherUserProfileViewModel
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.ProfileViewModel

val featureProfilesModule = DI {
    bind<AcceptFriendRequestUseCase>() with provider {
        AcceptFriendRequestUseCaseImpl(instance(), instance())
    }
    bind<DenyFriendRequestUseCase>() with provider {
        DenyFriendRequestUseCaseImpl(instance(), instance())
    }
    bind<GetFriendRequestsUseCase>() with provider {
        GetFriendRequestsUseCaseImpl(instance(), instance(), instance())
    }
    bind<GetFriendStatusUseCase>() with provider {
        GetFriendStatusUseCaseImpl(instance(), instance(), instance())
    }
    bind<SendFriendRequestUseCase>() with provider {
        SendFriendRequestUseCaseImpl(instance(), instance())
    }

    bind<ChangeCredentialsUseCase>() with provider {
        ChangeCredentialsUseCaseImpl(instance(), instance())
    }
    bind<ConfirmCredentialsUseCase>() with provider {
        ConfirmCredentialsUseCaseImpl(instance(), instance())
    }
    bind<SaveProfilePictureUseCase>() with provider {
        SaveProfilePictureUseCaseImpl(instance(), instance())
    }
    bind<SaveUserSettingsUseCase>() with provider {
        SaveUserSettingsUseCaseImpl(instance(), instance())
    }

    bind<GetCurrentUserLastResultsUseCase>() with provider {
        GetCurrentUserLastResultsUseCaseImpl(instance(), instance())
    }
    bind<GetCurrentUserUseCase>() with provider {
        GetCurrentUserUseCaseImpl(instance(), instance(), instance())
    }
    bind<GetUserLastResultsUseCase>() with provider {
        GetUserLastResultsUseCaseImpl(instance(), instance())
    }
    bind<GetUserUseCase>() with provider {
        GetUserUseCaseImpl(instance(), instance(), instance())
    }
    bind<LogoutUserUseCase>() with provider {
        LogoutUserUseCaseImpl(instance(), instance())
    }
    bind<SubscribeToProfileUpdatesUseCase>() with provider {
        SubscribeToProfileUpdatesUseCaseImpl(instance(), instance())
    }

    bind<FriendStatusUiModelMapper>() with provider {
        FriendStatusUiModelMapper()
    }
    bind<ResultUiModelMapper>() with provider {
        ResultUiModelMapper(instance())
    }
    bind<UserUiModelMapper>() with provider {
        UserUiModelMapper()
    }

    bindProvider {
        OtherUserProfileViewModel(
            getUserUseCase = instance(),
            sendFriendRequestUseCase = instance(),
            getFriendStatusUseCase = instance(),
            getUserLastResultsUseCase = instance(),
            userUiModelMapper = instance(),
            resultUiModelMapper = instance(),
            friendStatusUiModelMapper = instance()
        )
    }

    bindProvider {
        ProfileViewModel(
            getCurrentUserUseCase = instance(),
            logoutUserUseCase = instance(),
            saveProfilePictureUseCase = instance(),
            saveUserSettingsUseCase = instance(),
            changeCredentialsUseCase = instance(),
            confirmCredentialsUseCase = instance(),
            getFriendRequestsUseCase = instance(),
            acceptFriendRequestUseCase = instance(),
            denyFriendRequestUseCase = instance(),
            subscribeToProfileUpdatesUseCase = instance(),
            getCurrentUserLastResultsUseCase = instance(),
            userUiModelMapper = instance(),
            resultUiModelMapper = instance(),
            usernameValidator = instance(),
            passwordValidator = instance(),
            resourceManager = instance()
        )
    }
}