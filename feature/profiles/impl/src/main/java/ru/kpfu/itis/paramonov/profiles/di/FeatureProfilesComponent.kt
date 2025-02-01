package ru.kpfu.itis.paramonov.profiles.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.profiles.presentation.di.OtherUserProfileComponent
import ru.kpfu.itis.paramonov.profiles.presentation.di.ProfileComponent
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope
import ru.kpfu.itis.paramonov.profiles.api.api.FeatureProfilesApi

@Component(
    dependencies = [
        FeatureProfilesDependencies::class
    ]
)
@FeatureScope
interface FeatureProfilesComponent {

    fun profileComponentFactory(): ProfileComponent.Factory

    fun otherUserProfileComponentFactory(): OtherUserProfileComponent.Factory

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: FeatureProfilesDependencies): Builder

        @BindsInstance
        fun authenticationRouter(authenticationRouter: AuthenticationRouter): Builder

        fun build(): FeatureProfilesComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            FeatureProfilesApi::class
        ]
    )
    interface FeatureProfilesDependenciesComponent: FeatureProfilesDependencies
}