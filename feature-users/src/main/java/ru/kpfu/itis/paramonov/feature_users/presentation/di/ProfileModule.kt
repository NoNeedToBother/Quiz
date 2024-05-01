package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.ProfileViewModel
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
    fun provideQuestionSettingsViewModel(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        logoutUserUseCase: LogoutUserUseCase, authenticationRouter: AuthenticationRouter
    ): ViewModel {
        return ProfileViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            logoutUserUseCase = logoutUserUseCase,
            authenticationRouter = authenticationRouter
        )
    }
}