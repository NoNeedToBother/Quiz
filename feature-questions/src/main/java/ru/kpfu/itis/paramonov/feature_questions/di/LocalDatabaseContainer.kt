package ru.kpfu.itis.paramonov.feature_questions.di

import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi

interface LocalDatabaseContainer: FeatureContainer {

    fun localDatabaseApi(): LocalDatabaseApi
}