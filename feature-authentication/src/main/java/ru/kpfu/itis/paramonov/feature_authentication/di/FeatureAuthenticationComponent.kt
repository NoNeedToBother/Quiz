package ru.kpfu.itis.paramonov.feature_authentication.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.di.RegistrationComponent
import ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in.di.SigningInComponent
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.quiz.di.scopes.FeatureScope

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