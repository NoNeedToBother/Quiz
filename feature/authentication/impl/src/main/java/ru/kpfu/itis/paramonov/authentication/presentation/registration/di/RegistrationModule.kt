package ru.kpfu.itis.paramonov.authentication.presentation.registration.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.authentication.domain.usecase.CheckUserIsAuthenticatedUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.domain.usecase.RegisterUserUseCaseImpl
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.authentication.presentation.registration.RegisterViewModel
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator

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
    fun registerUserUseCase(impl: RegisterUserUseCaseImpl): RegisterUserUseCase = impl

    @Provides
    fun checkUserIsAuthenticatedUseCase(impl: CheckUserIsAuthenticatedUseCaseImpl): CheckUserIsAuthenticatedUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun provideRegisterViewModel(
        registerUserUseCase: RegisterUserUseCase,
        checkUserIsAuthenticatedUseCase: CheckUserIsAuthenticatedUseCase,
        mapper: UserUiModelMapper,
        usernameValidator: UsernameValidator,
        passwordValidator: PasswordValidator
    ): ViewModel {
        return RegisterViewModel(
            registerUserUseCase = registerUserUseCase,
            checkUserIsAuthenticatedUseCase = checkUserIsAuthenticatedUseCase,
            mapper = mapper,
            usernameValidator = usernameValidator,
            passwordValidator = passwordValidator
        )
    }
}