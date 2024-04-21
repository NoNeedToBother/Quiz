package ru.kpfu.itis.paramonov.feature_questions.di

import dagger.Component
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di.QuestionComponent
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.di.QuestionSettingsComponent
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.question_api.domain.api.QuestionApi
import ru.kpfu.itis.paramonov.quiz.di.scopes.FeatureScope

@Component(
    dependencies = [
        FeatureQuestionsDependencies::class
    ]
)
@FeatureScope
interface FeatureQuestionsComponent {
    fun questionSettingsComponentFactory(): QuestionSettingsComponent.Factory

    fun questionsComponentFactory(): QuestionComponent.Factory

    @Component.Builder
    interface Builder {

        fun dependencies(dependencies: FeatureQuestionsDependencies): Builder

        fun build(): FeatureQuestionsComponent
    }

    @Component(
        dependencies = [
            LocalDatabaseApi::class,
            QuestionApi::class,
            CommonApi::class,
        ]
    )
    interface FeatureQuestionsDependenciesComponent : FeatureQuestionsDependencies

}