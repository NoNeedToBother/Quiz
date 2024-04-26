package ru.kpfu.itis.paramonov.local_database_api.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.paramonov.local_database_api.data.room.dao.AnswerDao
import ru.kpfu.itis.paramonov.local_database_api.data.room.dao.QuestionDao
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.AnswerEntity
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.QuestionEntity

@Database(
    version = 1,
    entities = [
        QuestionEntity::class,
        AnswerEntity::class
    ]
)
abstract class QuestionDatabase: RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    abstract fun answerDao(): AnswerDao

    companion object {
        fun init(context: Context, name: String): QuestionDatabase {
            return Room.databaseBuilder(context, QuestionDatabase::class.java, name)
                .build()
        }
    }
}