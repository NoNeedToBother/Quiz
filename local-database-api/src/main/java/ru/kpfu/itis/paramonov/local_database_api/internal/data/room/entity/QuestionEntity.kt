package ru.kpfu.itis.paramonov.local_database_api.internal.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    indices = [Index(value = ["text"], unique = true)]
)
internal data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val difficulty: String,
    val category: String
) {
}