package ru.kpfu.itis.paramonov.local_database_api.internal.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

internal data class QuestionWithAnswers(
    @Embedded
    val question: QuestionEntity,
    @Relation(parentColumn = "id", entityColumn = "question_id")
    val answers: List<AnswerEntity>
)