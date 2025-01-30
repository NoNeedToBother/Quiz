package ru.kpfu.itis.paramonov.users.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope
import ru.kpfu.itis.paramonov.users.presentation.di.FriendsComponent
import ru.kpfu.itis.paramonov.users.presentation.di.SearchUsersComponent
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository

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
            UserRepository::class
        ]
    )
    interface FeatureUsersDependenciesComponent: FeatureUsersDependencies

}