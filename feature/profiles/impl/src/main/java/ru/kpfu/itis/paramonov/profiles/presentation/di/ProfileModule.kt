package ru.kpfu.itis.paramonov.profiles.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.ProfileViewModel
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class ProfileModule {

    @Provides
    fun profileViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): ProfileViewModel {
        return ViewModelProvider(fragment, factory)[ProfileViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun provideProfileViewModel(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        logoutUserUseCase: LogoutUserUseCase,
        saveProfilePictureUseCase: SaveProfilePictureUseCase,
        saveUserSettingsUseCase: SaveUserSettingsUseCase,
        changeCredentialsUseCase: ChangeCredentialsUseCase,
        confirmCredentialsUseCase: ConfirmCredentialsUseCase,
        getFriendRequestsUseCase: GetFriendRequestsUseCase,
        acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
        denyFriendRequestUseCase: DenyFriendRequestUseCase,
        subscribeToProfileUpdatesUseCase: SubscribeToProfileUpdatesUseCase,
        getCurrentUserLastResultsUseCase: GetCurrentUserLastResultsUseCase,
        resultUiModelMapper: ResultUiModelMapper,
        userUiModelMapper: UserUiModelMapper,
        authenticationRouter: AuthenticationRouter
    ): ViewModel {
        return ProfileViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            logoutUserUseCase = logoutUserUseCase,
            saveProfilePictureUseCase = saveProfilePictureUseCase,
            authenticationRouter = authenticationRouter,
            saveUserSettingsUseCase = saveUserSettingsUseCase,
            changeCredentialsUseCase = changeCredentialsUseCase,
            confirmCredentialsUseCase = confirmCredentialsUseCase,
            getFriendRequestsUseCase = getFriendRequestsUseCase,
            acceptFriendRequestUseCase = acceptFriendRequestUseCase,
            denyFriendRequestUseCase = denyFriendRequestUseCase,
            subscribeToProfileUpdatesUseCase = subscribeToProfileUpdatesUseCase,
            getCurrentUserLastResultsUseCase = getCurrentUserLastResultsUseCase,
            resultUiModelMapper = resultUiModelMapper,
            userUiModelMapper = userUiModelMapper
        )
    }
}