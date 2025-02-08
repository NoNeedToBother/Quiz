package ru.kpfu.itis.paramonov.database.external.domain.repository

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

interface QuestionSettingsRepository {

    fun getString(key: String): String

    fun saveString(key: String, str: String)

    fun getDifficulty(): Difficulty

    fun saveDifficulty(difficulty: Difficulty)

    fun getCategory(): Category

    fun saveCategory(category: Category)

    fun getGameMode(): GameMode

    fun saveGameMode(gameMode: GameMode)

    fun getLimit(): Int

    fun saveLimit(limit: Int)
}
