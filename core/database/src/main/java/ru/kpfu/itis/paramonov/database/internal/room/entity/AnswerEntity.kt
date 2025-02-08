package ru.kpfu.itis.paramonov.database.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
internal data class AnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    @ColumnInfo(name = "is_correct")
    val correct: Boolean,
    @ColumnInfo(name = "question_id")
    val questionId: Long
)
