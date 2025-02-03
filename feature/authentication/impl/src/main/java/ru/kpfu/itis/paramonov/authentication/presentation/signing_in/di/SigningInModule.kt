package ru.kpfu.itis.paramonov.authentication.presentation.signing_in.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.authentication.api.usecase.AuthenticateUserUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.authentication.domain.usecase.AuthenticateUserUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.domain.usecase.CheckUserIsAuthenticatedUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.SignInViewModel
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class SigningInModule {

    @Provides
    fun signInViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): SignInViewModel {
        return ViewModelProvider(fragment, factory)[SignInViewModel::class.java]
    }

    @Provides
    fun authenticateUserUseCase(impl: AuthenticateUserUseCaseImpl): AuthenticateUserUseCase = impl

    @Provides
    fun checkUserIsAuthenticatedUseCase(impl: CheckUserIsAuthenticatedUseCaseImpl): CheckUserIsAuthenticatedUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    fun provideSignInViewModel(
        authenticateUserUseCase: AuthenticateUserUseCase,
        checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
        usernameValidator: UsernameValidator,
        passwordValidator: PasswordValidator,
        mapper: UserUiModelMapper
    ): ViewModel {
        return SignInViewModel(
            authenticateUserUseCase = authenticateUserUseCase,
            checkUserIsAuthenticatedUseCase = checkUserIsAuthenticatedUseCase,
            usernameValidator = usernameValidator,
            passwordValidator = passwordValidator,
            mapper = mapper
        )
    }
}