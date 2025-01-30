package ru.kpfu.itis.paramonov.questions.di

import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.questions.presentation.questions.di.QuestionsComponent
import ru.kpfu.itis.paramonov.questions.presentation.questions.di.TrainingQuestionsComponent
import ru.kpfu.itis.paramonov.questions.presentation.settings.di.QuestionSettingsComponent
import ru.kpfu.itis.paramonov.questions.presentation.settings.di.TrainingQuestionSettingsComponent
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository
import ru.kpfu.itis.paramonov.questions.presentation.questions.di.ResultComponent

@Component(
    dependencies = [
        FeatureQuestionsDependencies::class
    ]
)
@FeatureScope
interface FeatureQuestionsComponent {
    fun questionSettingsComponentFactory(): QuestionSettingsComponent.Factory

    fun questionsComponentFactory(): QuestionsComponent.Factory

    fun trainingQuestionsComponentFactory(): TrainingQuestionsComponent.Factory

    fun trainingQuestionSettingsComponentFactory(): TrainingQuestionSettingsComponent.Factory

    fun resultComponentFactory(): ResultComponent.Factory

    @Component.Builder
    interface Builder {

        fun dependencies(dependencies: FeatureQuestionsDependencies): Builder

        @BindsInstance
        fun mainMenuRouter(mainMenuRouter: MainMenuRouter): Builder

        @BindsInstance
        fun questionsRouter(questionsRouter: QuestionsRouter): Builder

        fun build(): FeatureQuestionsComponent
    }

    @Component(
        dependencies = [
            QuestionSettingsRepository::class,
            SavedQuestionRepository::class,
            QuestionRepository::class,
            ResultRepository::class,
            UserRepository::class,
            CommonApi::class,
        ]
    )
    interface FeatureQuestionsDependenciesComponent : FeatureQuestionsDependencies

}