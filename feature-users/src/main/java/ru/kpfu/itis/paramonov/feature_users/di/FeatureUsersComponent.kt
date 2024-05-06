package ru.kpfu.itis.paramonov.feature_users.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.feature_users.presentation.di.OtherUserProfileComponent
import ru.kpfu.itis.paramonov.feature_users.presentation.di.ProfileComponent
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope

@Component(
    dependencies = [
        FeatureUsersDependencies::class
    ]
)
@FeatureScope
interface FeatureUsersComponent {

    fun profileComponentFactory(): ProfileComponent.Factory

    fun otherUserProfileComponentFactory(): OtherUserProfileComponent.Factory

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: FeatureUsersDependencies): Builder

        @BindsInstance
        fun authenticationRouter(authenticationRouter: AuthenticationRouter): Builder

        fun build(): FeatureUsersComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            FirebaseApi::class
        ]
    )
    interface FeatureUsersDependenciesComponent: FeatureUsersDependencies
}