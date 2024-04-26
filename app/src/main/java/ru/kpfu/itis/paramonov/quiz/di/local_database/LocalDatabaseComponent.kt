package ru.kpfu.itis.paramonov.quiz.di.local_database

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.local_database_api.di.LocalDatabaseModule
import ru.kpfu.itis.paramonov.local_database_api.di.RoomModule
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import javax.inject.Singleton

@Component(
    modules = [
        RoomModule::class,
        LocalDatabaseModule::class,
    ],
    dependencies = [
        LocalDatabaseDependencies::class
    ]
)
@Singleton
interface LocalDatabaseComponent: LocalDatabaseApi {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface LocalDatabaseDependenciesComponent : LocalDatabaseDependencies
}