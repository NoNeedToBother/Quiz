package ru.kpfu.itis.paramonov.quiz.di.local_database

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.local_database_api.data.repository.SharedPreferencesRepositoryImpl
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SharedPreferencesRepository

@Module
class DatabaseModule {

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
    fun sharedPreferencesRepositoryImpl(sharedPreferences: SharedPreferences
    ): SharedPreferencesRepositoryImpl {
        return SharedPreferencesRepositoryImpl(sharedPreferences)
    }

    @Provides
    fun sharedPreferencesRepository(
        impl: SharedPreferencesRepositoryImpl
    ): SharedPreferencesRepository = impl
}