package ru.kpfu.itis.paramonov.profiles.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
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
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserLastResultsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.LogoutUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.SubscribeToProfileUpdatesUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.AcceptFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.DenyFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendRequestsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.ChangeCredentialsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.ConfirmCredentialsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.SaveProfilePictureUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.SaveUserSettingsUseCaseImpl

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
    fun getCurrentUserUseCase(impl: GetCurrentUserUseCaseImpl): GetCurrentUserUseCase = impl

    @Provides
    fun logoutUserUseCase(impl: LogoutUserUseCaseImpl): LogoutUserUseCase = impl

    @Provides
    fun saveProfilePictureUseCase(impl: SaveProfilePictureUseCaseImpl): SaveProfilePictureUseCase = impl

    @Provides
    fun saveUserSettingsUseCase(impl: SaveUserSettingsUseCaseImpl): SaveUserSettingsUseCase = impl

    @Provides
    fun changeCredentialsUseCase(impl: ChangeCredentialsUseCaseImpl): ChangeCredentialsUseCase = impl

    @Provides
    fun confirmCredentialsUseCase(impl: ConfirmCredentialsUseCaseImpl): ConfirmCredentialsUseCase = impl

    @Provides
    fun getFriendRequestsUseCase(impl: GetFriendRequestsUseCaseImpl): GetFriendRequestsUseCase = impl

    @Provides
    fun acceptFriendRequestUseCase(impl: AcceptFriendRequestUseCaseImpl): AcceptFriendRequestUseCase = impl

    @Provides
    fun denyFriendRequestUseCase(impl: DenyFriendRequestUseCaseImpl): DenyFriendRequestUseCase = impl

    @Provides
    fun subscribeToProfileUpdatesUseCase(impl: SubscribeToProfileUpdatesUseCaseImpl): SubscribeToProfileUpdatesUseCase = impl

    @Provides
    fun getCurrentUserLastResultsUseCase(impl: GetCurrentUserLastResultsUseCaseImpl): GetCurrentUserLastResultsUseCase = impl

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
        usernameValidator: UsernameValidator,
        passwordValidator: PasswordValidator,
        resourceManager: ResourceManager
    ): ViewModel {
        return ProfileViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            logoutUserUseCase = logoutUserUseCase,
            saveProfilePictureUseCase = saveProfilePictureUseCase,
            saveUserSettingsUseCase = saveUserSettingsUseCase,
            changeCredentialsUseCase = changeCredentialsUseCase,
            confirmCredentialsUseCase = confirmCredentialsUseCase,
            getFriendRequestsUseCase = getFriendRequestsUseCase,
            acceptFriendRequestUseCase = acceptFriendRequestUseCase,
            denyFriendRequestUseCase = denyFriendRequestUseCase,
            subscribeToProfileUpdatesUseCase = subscribeToProfileUpdatesUseCase,
            getCurrentUserLastResultsUseCase = getCurrentUserLastResultsUseCase,
            resultUiModelMapper = resultUiModelMapper,
            userUiModelMapper = userUiModelMapper,
            usernameValidator = usernameValidator,
            passwordValidator = passwordValidator,
            resourceManager = resourceManager
        )
    }
}