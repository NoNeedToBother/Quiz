package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.question_api.domain.api.QuestionApi

abstract class QuestionsFeatureApiHolder(
    private val mFeatureContainer: FeatureQuestionsDependenciesContainer
): FeatureApiHolder(mFeatureContainer) {

    fun localDatabaseApi(): LocalDatabaseApi {
        return mFeatureContainer.localDatabaseApi()
    }

    fun questionApi(): QuestionApi {
        return mFeatureContainer.questionApi()
    }
}