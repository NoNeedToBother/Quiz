package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.RegisterViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class RegistrationModule {

    @Provides
    fun provideMainViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): RegisterViewModel {
        return ViewModelProvider(fragment, factory)[RegisterViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun provideRegisterViewModel(
        registerUserUseCase: RegisterUserUseCase,
        getCurrentUserUseCase: GetCurrentUserUseCase
        ): ViewModel {
        return RegisterViewModel(registerUserUseCase, getCurrentUserUseCase)
    }
}