package ru.kpfu.itis.paramonov.local_database_api.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.local_database_api.data.room.database.AppDatabase

@Module
class RoomModule {

    @RoomDatabaseNameKey
    @Provides
    fun roomDatabaseKey(): String = "quiz_db"

    @Provides
    fun database(
        context: Context,
        @RoomDatabaseNameKey name: String
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, name)
            .build()
    }
}