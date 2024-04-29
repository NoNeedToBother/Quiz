package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import javax.inject.Inject

@ApplicationScope
class FeatureQuestionsHolder @Inject constructor(
    featureContainer: FeatureQuestionsDependenciesContainer,
    private val mainMenuRouter: MainMenuRouter,
    private val questionsRouter: QuestionsRouter
): QuestionsFeatureApiHolder(featureContainer){
    override fun initializeDependencies(): Any {
        val featureQuestionsDependencies = DaggerFeatureQuestionsComponent_FeatureQuestionsDependenciesComponent.builder()
            .commonApi(commonApi())
            .localDatabaseApi(localDatabaseApi())
            .questionApi(questionApi())
            .firebaseApi(firebaseApi())
            .build()
        return DaggerFeatureQuestionsComponent.builder()
            .dependencies(featureQuestionsDependencies)
            .mainMenuRouter(mainMenuRouter)
            .questionsRouter(questionsRouter)
            .build()
    }
}