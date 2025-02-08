package ru.kpfu.itis.paramonov.database.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.kpfu.itis.paramonov.database.internal.room.entity.AnswerEntity

@Dao
internal interface AnswerDao {

    @Insert
    fun save(answer: AnswerEntity)
}
