package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.RegisterViewModel
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class RegistrationModule {

    @Provides
    fun registerViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): RegisterViewModel {
        return ViewModelProvider(fragment, factory)[RegisterViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun provideRegisterViewModel(
        registerUserUseCase: RegisterUserUseCase,
        checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
        authenticationRouter: AuthenticationRouter,
        mainMenuRouter: MainMenuRouter
    ): ViewModel {
        return RegisterViewModel(registerUserUseCase, checkUserIsAuthenticatedUseCase,
            authenticationRouter, mainMenuRouter)
    }
}