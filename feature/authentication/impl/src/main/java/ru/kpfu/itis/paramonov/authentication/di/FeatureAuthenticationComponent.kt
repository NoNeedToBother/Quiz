package ru.kpfu.itis.paramonov.authentication.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.authentication.presentation.registration.di.RegistrationComponent
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.di.SigningInComponent
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope

@Component(
    dependencies = [
        FeatureAuthenticationDependencies::class
    ]
)
@FeatureScope
interface FeatureAuthenticationComponent {

    fun registrationComponentFactory(): RegistrationComponent.Factory

    fun signingInComponentFactory(): SigningInComponent.Factory

    @Component.Builder
    interface Builder {

        fun dependencies(dependencies: FeatureAuthenticationDependencies): Builder

        @BindsInstance
        fun authenticationRouter(authenticationRouter: AuthenticationRouter): Builder

        @BindsInstance
        fun mainMenuRouter(mainMenuRouter: MainMenuRouter): Builder

        fun build(): FeatureAuthenticationComponent
    }

    @Component(
        dependencies = [
            AuthenticationRepository::class,
            CommonApi::class,
        ]
    )
    interface FeatureAuthenticationDependenciesComponent : FeatureAuthenticationDependencies
}