package ru.kpfu.itis.paramonov.local_database_api.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.AnswerEntity

@Dao
interface AnswerDao {

    @Insert
    fun save(answer: AnswerEntity)
}