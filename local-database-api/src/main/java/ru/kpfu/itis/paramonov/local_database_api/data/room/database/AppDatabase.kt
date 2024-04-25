package ru.kpfu.itis.paramonov.local_database_api.data.room.database

import androidx.room.Database
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
abstract class AppDatabase: RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    abstract fun answerDao(): AnswerDao
}