package ru.kpfu.itis.paramonov.local_database_api.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.local_database_api.data.room.database.QuestionDatabase
import ru.kpfu.itis.paramonov.local_database_api.data.room.repository.SavedQuestionRepositoryImpl
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository
import javax.inject.Singleton

@Module
class RoomModule {

    @RoomDatabaseNameKey
    @Provides
    fun roomDatabaseKey(): String = "quiz"

    @Provides
    @Singleton
    fun database(
        context: Context,
        //@RoomDatabaseNameKey name: String
    ): QuestionDatabase {
        return QuestionDatabase.init(context, "quiz")
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