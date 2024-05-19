package ru.kpfu.itis.paramonov.feature_profiles.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.feature_profiles.presentation.di.OtherUserProfileComponent
import ru.kpfu.itis.paramonov.feature_profiles.presentation.di.ProfileComponent
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope

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
            FirebaseApi::class
        ]
    )
    interface FeatureProfilesDependenciesComponent: FeatureProfilesDependencies
}