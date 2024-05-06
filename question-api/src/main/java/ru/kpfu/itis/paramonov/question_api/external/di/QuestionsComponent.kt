package ru.kpfu.itis.paramonov.question_api.external.di

import dagger.Component
import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.question_api.external.domain.api.QuestionApi
import ru.kpfu.itis.paramonov.question_api.internal.di.QuestionModule

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