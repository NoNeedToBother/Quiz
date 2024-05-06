package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.local_database_api.external.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.question_api.external.domain.api.QuestionApi

abstract class FeatureQuestionsApiHolder(
    private val mFeatureContainer: FeatureQuestionsDependenciesContainer
): FeatureApiHolder(mFeatureContainer) {

    fun localDatabaseApi(): LocalDatabaseApi {
        return mFeatureContainer.localDatabaseApi()
    }

    fun questionApi(): QuestionApi {
        return mFeatureContainer.questionApi()
    }

    fun firebaseApi(): FirebaseApi {
        return mFeatureContainer.firebaseApi()
    }
}