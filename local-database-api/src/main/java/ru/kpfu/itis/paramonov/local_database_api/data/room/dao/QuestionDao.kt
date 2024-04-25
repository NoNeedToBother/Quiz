package ru.kpfu.itis.paramonov.local_database_api.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.kpfu.itis.paramonov.local_database_api.data.room.database.AppDatabase
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.QuestionEntity
import ru.kpfu.itis.paramonov.local_database_api.data.room.entity.QuestionWithAnswers

@Dao
abstract class QuestionDao(database: AppDatabase) {

    private val answerDao: AnswerDao

    init {
        answerDao = database.answerDao()
    }

    @Query("select * from answers")
    @Transaction
    abstract fun getAll(): List<QuestionWithAnswers>

    @Insert
    abstract fun save(question: QuestionEntity)

    fun save(question: QuestionWithAnswers) {
        save(question.question)
        for (answer in question.answers) {
            answerDao.save(answer)
        }
    }
}