package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.AuthenticateUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in.SignInViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class SigningInModule {

    @Provides
    fun provideMainViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): SignInViewModel {
        return ViewModelProvider(fragment, factory)[SignInViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    fun provideSignInViewModel(
        authenticateUserUseCase: AuthenticateUserUseCase,
        getCurrentUserUseCase: GetCurrentUserUseCase
        ): ViewModel {
        return SignInViewModel(authenticateUserUseCase, getCurrentUserUseCase)
    }
}