package ru.kpfu.itis.paramonov.quiz.di.firebase

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependencies

@Component(
    modules = [
        FirebaseModule::class
    ],
    dependencies = [
        FirebaseDependencies::class
    ]
)
@ApplicationScope
interface FirebaseComponent: FirebaseApi, ComponentDependencies {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface FirebaseDependenciesComponent : FirebaseDependencies
}