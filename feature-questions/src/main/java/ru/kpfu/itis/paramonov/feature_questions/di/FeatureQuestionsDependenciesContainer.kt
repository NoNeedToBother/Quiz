package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.database.external.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.network.external.domain.api.QuestionApi

interface FeatureQuestionsDependenciesContainer: FeatureContainer {
    fun localDatabaseApi(): LocalDatabaseApi

    fun questionApi(): QuestionApi

    fun firebaseApi(): FirebaseApi
}