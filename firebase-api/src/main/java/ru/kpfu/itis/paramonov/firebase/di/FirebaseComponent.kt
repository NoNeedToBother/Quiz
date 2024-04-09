package ru.kpfu.itis.paramonov.firebase.di

import dagger.Component
import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi

@Component(
    modules = [
        FirebaseModule::class
    ],
    dependencies = [
        FirebaseDependencies::class
    ]
)
interface FirebaseComponent: FirebaseApi {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface FirebaseDependenciesComponent : FirebaseDependencies
}