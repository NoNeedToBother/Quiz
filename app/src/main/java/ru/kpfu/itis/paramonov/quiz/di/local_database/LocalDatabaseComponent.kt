package ru.kpfu.itis.paramonov.quiz.di.local_database

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi

@Component(
    modules = [
        LocalDatabaseModule::class
    ],
    dependencies = [
        LocalDatabaseDependencies::class
    ]
)
interface LocalDatabaseComponent: LocalDatabaseApi {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface LocalDatabaseDependenciesComponent : LocalDatabaseDependencies
}