package ru.kpfu.itis.paramonov.feature_users.di

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.quiz.di.scopes.FeatureScope

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