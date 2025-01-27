package ru.kpfu.itis.paramonov.database.external.di

import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.core.api.ContextApi
import ru.kpfu.itis.paramonov.database.external.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.database.internal.di.LocalDatabaseModule
import ru.kpfu.itis.paramonov.database.internal.di.RoomModule
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
            ContextApi::class,
            CommonApi::class,
        ]
    )
    interface LocalDatabaseDependenciesComponent : LocalDatabaseDependencies
}