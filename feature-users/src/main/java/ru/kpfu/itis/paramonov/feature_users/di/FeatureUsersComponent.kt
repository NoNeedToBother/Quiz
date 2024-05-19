package ru.kpfu.itis.paramonov.feature_users.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.scopes.FeatureScope
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.navigation.UserRouter

@Component(
    dependencies = [
        FeatureUsersDependencies::class
    ]
)
@FeatureScope
interface FeatureUsersComponent {

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