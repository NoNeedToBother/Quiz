package ru.kpfu.itis.paramonov.questions.di

import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import javax.inject.Inject

@ApplicationScope
class FeatureQuestionsHolder @Inject constructor(
    private val dependenciesContainer: FeatureQuestionsDependenciesContainer,
    private val mainMenuRouter: MainMenuRouter,
    private val questionsRouter: QuestionsRouter
): FeatureApiHolder(dependenciesContainer){
    override fun initializeDependencies(): Any {
        val featureQuestionsDependencies = DaggerFeatureQuestionsComponent_FeatureQuestionsDependenciesComponent.builder()
            .commonApi(commonApi())
            .featureQuestionsApi(dependenciesContainer.featureQuestionsApi())
            .build()
        return DaggerFeatureQuestionsComponent.builder()
            .dependencies(featureQuestionsDependencies)
            .mainMenuRouter(mainMenuRouter)
            .questionsRouter(questionsRouter)
            .build()
    }
}