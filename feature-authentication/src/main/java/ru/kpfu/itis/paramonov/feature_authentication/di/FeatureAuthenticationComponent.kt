package ru.kpfu.itis.paramonov.feature_authentication.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.di.RegistrationComponent
import ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in.di.SigningInComponent
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
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
            FirebaseApi::class,
            CommonApi::class,
        ]
    )
    interface FeatureAuthenticationDependenciesComponent : FeatureAuthenticationDependencies
}