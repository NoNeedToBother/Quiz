package ru.kpfu.itis.paramonov.local_database_api.domain.repository

import ru.kpfu.itis.paramonov.local_database_api.domain.model.Category
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Difficulty
import ru.kpfu.itis.paramonov.local_database_api.domain.model.GameMode

interface SharedPreferencesRepository {

    fun getString(key: String): String

    fun saveString(key: String, str: String)

    fun getDifficulty(): Difficulty

    fun saveDifficulty(difficulty: Difficulty)

    fun getCategory(): Category

    fun saveCategory(category: Category)

    fun getGameMode(): GameMode

    fun saveGameMode(gameMode: GameMode)
}