package ru.kpfu.itis.paramonov.local_database_api.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.local_database_api.data.shared_pref.repository.QuestionSettingsRepositoryImpl
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository

@Module
class LocalDatabaseModule {

    @SharedPreferencesKey
    @Provides
    fun sharedPreferencesKey() = "quiz_database"

    @Provides
    fun sharedPreferences(
        @SharedPreferencesKey sharedPreferencesKey: String,
        context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    }

    @Provides
    fun sharedPreferencesRepositoryImpl(
        sharedPreferences: SharedPreferences,
        resourceManager: ResourceManager
    ): QuestionSettingsRepositoryImpl {
        return QuestionSettingsRepositoryImpl(sharedPreferences, resourceManager)
    }

    @Provides
    fun sharedPreferencesRepository(
        impl: QuestionSettingsRepositoryImpl
    ): QuestionSettingsRepository = impl
}