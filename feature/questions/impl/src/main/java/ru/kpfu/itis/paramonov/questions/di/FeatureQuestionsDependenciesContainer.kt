package ru.kpfu.itis.paramonov.questions.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.questions.api.api.FeatureQuestionsApi

interface FeatureQuestionsDependenciesContainer: FeatureContainer {

    fun featureQuestionsApi(): FeatureQuestionsApi

}