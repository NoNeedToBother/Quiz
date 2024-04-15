package ru.kpfu.itis.paramonov.local_database_api.data.repository

import android.content.SharedPreferences
import ru.kpfu.itis.paramonov.local_database_api.data.repository.exception.NoParameterFoundException
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Category
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Difficulty
import ru.kpfu.itis.paramonov.local_database_api.domain.model.GameMode
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
): SharedPreferencesRepository {

    override fun getString(key: String): String {
        val res = sharedPreferences.getString(key, NO_DATA)
        if (res == null || res == NO_DATA)
            throw NoParameterFoundException()
        else return res
    }

    override fun saveString(key: String, str: String) {
        sharedPreferences.edit().apply {
            putString(key, str)
            apply()
        }
    }

    override fun getDifficulty(): Difficulty {
        val res = getString(DIFFICULTY_KEY)
        return Difficulty.valueOf(res.uppercase())
    }

    override fun saveDifficulty(difficulty: Difficulty) {
        val difficultyStr = difficulty.name.lowercase()
        saveString(DIFFICULTY_KEY, difficultyStr)
    }

    override fun getCategory(): Category {
        val res = getString(CATEGORY_KEY)
        return Category.valueOf(res.uppercase())
    }

    override fun saveCategory(category: Category) {
        val categoryStr = category.name.lowercase()
        saveString(CATEGORY_KEY, categoryStr)
    }

    override fun getGameMode(): GameMode {
        val res = getString(GAME_MODE_KEY)
        return GameMode.valueOf(res.uppercase())
    }

    override fun saveGameMode(gameMode: GameMode) {
        val gameModeStr = gameMode.name.lowercase()
        saveString(DIFFICULTY_KEY, gameModeStr)
    }

    companion object {
        private const val CATEGORY_KEY = "category"
        private const val DIFFICULTY_KEY = "difficulty"
        private const val GAME_MODE_KEY = "game_mode"
        private const val NO_DATA = "NO_DATA"
    }
}