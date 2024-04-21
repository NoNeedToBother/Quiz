package ru.kpfu.itis.paramonov.quiz.di.dependencies

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationHolder
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependenciesContainer
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependenciesContainer
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsHolder
import ru.kpfu.itis.paramonov.quiz.App

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun featureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    fun featureAuthenticationDependenciesContainer(application: App): FeatureAuthenticationDependenciesContainer

    @ApplicationScope
    @Binds
    fun featureQuestionsDependenciesContainer(application: App): FeatureQuestionsDependenciesContainer

    @ApplicationScope
    @Binds
    @ClassKey(FeatureAuthenticationDependencies::class)
    @IntoMap
    fun authenticationFeatureHolder(authenticationFeatureHolder: FeatureAuthenticationHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FeatureQuestionsDependencies::class)
    @IntoMap
    fun questionsFeatureHolder(questionsFeatureHolder: FeatureQuestionsHolder): FeatureApiHolder
}