package ru.kpfu.itis.paramonov.database.internal.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.kpfu.itis.paramonov.database.internal.data.room.entity.AnswerEntity

@Dao
internal interface AnswerDao {

    @Insert
    fun save(answer: AnswerEntity)
}