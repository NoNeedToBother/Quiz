package ru.kpfu.itis.paramonov.network.external.di

import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.network.external.domain.api.QuestionApi
import ru.kpfu.itis.paramonov.network.internal.di.QuestionModule

@Component(
    modules = [
        QuestionModule::class
    ],
    dependencies = [
        QuestionsDependencies::class
    ]
)
@ApplicationScope
interface QuestionsComponent: QuestionApi {
    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface QuestionDependenciesComponent: QuestionsDependencies
}