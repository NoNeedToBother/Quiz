package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi

abstract class QuestionsFeatureApiHolder(
    private val mFeatureContainer: LocalDatabaseContainer
): FeatureApiHolder(mFeatureContainer) {

    fun localDatabaseApi(): LocalDatabaseApi {
        return mFeatureContainer.localDatabaseApi()
    }
}