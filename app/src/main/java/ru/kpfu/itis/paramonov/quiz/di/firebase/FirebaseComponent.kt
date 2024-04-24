package ru.kpfu.itis.paramonov.quiz.di.firebase

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.firebase.di.FirebaseModule
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi

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