package ru.kpfu.itis.paramonov.local_database_api.external.domain.repository

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode

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