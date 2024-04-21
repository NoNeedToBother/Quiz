package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FeatureQuestionsHolder @Inject constructor(
    featureContainer: FeatureQuestionsDependenciesContainer
): QuestionsFeatureApiHolder(featureContainer){
    override fun initializeDependencies(): Any {
        val featureQuestionsDependencies = DaggerFeatureQuestionsComponent_FeatureQuestionsDependenciesComponent.builder()
            .commonApi(commonApi())
            .localDatabaseApi(localDatabaseApi())
            .questionApi(questionApi())
            .build()
        return DaggerFeatureQuestionsComponent.builder()
            .dependencies(featureQuestionsDependencies)
            .build()
    }
}