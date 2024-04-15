package ru.kpfu.itis.paramonov.quiz.di.local_database

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi

@Component(
    modules = [
        DatabaseModule::class
    ],
    dependencies = [
        DatabaseDependencies::class
    ]
)
interface DatabaseComponent {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface DatabaseDependenciesComponent : DatabaseDependencies
}