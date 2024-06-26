package ru.kpfu.itis.paramonov.local_database_api.internal.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.kpfu.itis.paramonov.local_database_api.internal.data.room.entity.QuestionEntity
import ru.kpfu.itis.paramonov.local_database_api.internal.data.room.entity.QuestionWithAnswers

@Dao
internal abstract class QuestionDao {

    @Query("select * from questions order by random() limit :limit")
    @Transaction
    abstract fun getQuestions(limit: Int): List<QuestionWithAnswers>

    @Insert
    abstract fun save(question: QuestionEntity): Long
}