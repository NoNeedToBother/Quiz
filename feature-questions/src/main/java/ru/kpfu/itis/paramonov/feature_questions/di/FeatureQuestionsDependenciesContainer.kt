package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.question_api.domain.api.QuestionApi

interface FeatureQuestionsDependenciesContainer: FeatureContainer {
    fun localDatabaseApi(): LocalDatabaseApi

    fun questionApi(): QuestionApi
}