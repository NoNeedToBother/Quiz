package ru.kpfu.itis.paramonov.firebase.external.di

import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.firebase.internal.di.FirebaseModule
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi

@Component(
    modules = [
        FirebaseModule::class
    ],
    dependencies = [
        FirebaseDependencies::class
    ]
)
@ApplicationScope
interface FirebaseComponent: FirebaseApi {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface FirebaseDependenciesComponent : FirebaseDependencies
}