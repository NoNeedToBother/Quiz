package ru.kpfu.itis.paramonov.feature_users.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope
import ru.kpfu.itis.paramonov.feature_users.presentation.di.FriendsComponent
import ru.kpfu.itis.paramonov.feature_users.presentation.di.SearchUsersComponent
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.navigation.UserRouter

@Component(
    dependencies = [
        FeatureUsersDependencies::class
    ]
)
@FeatureScope
interface FeatureUsersComponent {

    fun searchUsersComponentFactory(): SearchUsersComponent.Factory

    fun friendsComponentFactory(): FriendsComponent.Factory

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: FeatureUsersDependencies): Builder

        fun userRouter(@BindsInstance userRouter: UserRouter): Builder

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