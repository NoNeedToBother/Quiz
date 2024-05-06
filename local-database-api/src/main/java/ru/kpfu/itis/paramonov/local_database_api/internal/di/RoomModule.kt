package ru.kpfu.itis.paramonov.local_database_api.internal.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.local_database_api.internal.data.room.database.QuestionDatabase
import ru.kpfu.itis.paramonov.local_database_api.internal.data.room.repository.SavedQuestionRepositoryImpl
import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.SavedQuestionRepository
import javax.inject.Singleton

@Module
internal class RoomModule {

    @RoomDatabaseNameKey
    @Provides
    fun roomDatabaseKey(): String = "quiz"

    @Provides
    @Singleton
    fun database(
        context: Context,
        @RoomDatabaseNameKey name: String
    ): QuestionDatabase {
        return QuestionDatabase.init(context, name)
    }

    @Provides
    fun savedQuestionRepositoryImpl(
        dispatcher: CoroutineDispatcher,
        questionDatabase: QuestionDatabase
    ): SavedQuestionRepositoryImpl {
        return SavedQuestionRepositoryImpl(
            dispatcher, questionDatabase
        )
    }

    @Provides
    fun savedQuestionRepository(impl: SavedQuestionRepositoryImpl): SavedQuestionRepository = impl
}