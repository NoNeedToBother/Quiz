package ru.kpfu.itis.paramonov.local_database_api.data.room.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.local_database_api.data.room.database.AppDatabase
import ru.kpfu.itis.paramonov.local_database_api.domain.model.question.QuestionModel
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository

class SavedQuestionRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val database: AppDatabase
): SavedQuestionRepository {
    override suspend fun getAllQuestions(): List<QuestionModel> {
        return listOf()
    }

    override suspend fun saveQuestions(questions: List<QuestionModel>) {
    }
}