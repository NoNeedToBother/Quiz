package ru.kpfu.itis.paramonov.quiz.di.questions

import dagger.Component
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.question_api.di.QuestionModule
import ru.kpfu.itis.paramonov.question_api.domain.api.QuestionApi

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