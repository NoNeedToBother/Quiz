package ru.kpfu.itis.paramonov.feature_profiles.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel.ProfileViewModel
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter

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
            getCurrentUserLastResultsUseCase = getCurrentUserLastResultsUseCase
        )
    }
}