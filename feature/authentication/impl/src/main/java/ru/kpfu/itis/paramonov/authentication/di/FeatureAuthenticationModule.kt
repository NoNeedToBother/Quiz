package ru.kpfu.itis.paramonov.authentication.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.authentication.api.usecase.AuthenticateUserUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.CheckUserIsAuthenticatedUseCase
import ru.kpfu.itis.paramonov.authentication.api.usecase.RegisterUserUseCase
import ru.kpfu.itis.paramonov.authentication.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.authentication.domain.usecase.AuthenticateUserUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.domain.usecase.CheckUserIsAuthenticatedUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.domain.usecase.RegisterUserUseCaseImpl
import ru.kpfu.itis.paramonov.authentication.presentation.registration.RegisterViewModel
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.SignInViewModel

val featureAuthenticationModule = DI {
    bind<AuthenticateUserUseCase>() with provider {
        AuthenticateUserUseCaseImpl(
            dispatcher = instance(),
            repository = instance()
        )
    }
    bind<CheckUserIsAuthenticatedUseCase>() with provider {
        CheckUserIsAuthenticatedUseCaseImpl(
            dispatcher = instance(),
            repository = instance()
        )
    }
    bind<RegisterUserUseCase>() with provider {
        RegisterUserUseCaseImpl(
            dispatcher = instance(),
            repository = instance()
        )
    }
    bind<UserUiModelMapper>() with provider { UserUiModelMapper() }

    bindProvider {
        RegisterViewModel(
            registerUserUseCase = instance(),
            checkUserIsAuthenticatedUseCase = instance(),
            mapper = instance(),
            usernameValidator = instance(),
            passwordValidator = instance(),
            resourceManager = instance()
        )
    }

    bindProvider {
        SignInViewModel(
            authenticateUserUseCase = instance(),
            checkUserIsAuthenticatedUseCase = instance(),
            mapper = instance(),
            passwordValidator = instance(),
            resourceManager = instance()
        )
    }
}