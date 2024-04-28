package ru.kpfu.itis.paramonov.local_database_api.data.shared_pref.repository

import android.content.SharedPreferences
import ru.kpfu.itis.paramonov.common.utils.toEnumName
import ru.kpfu.itis.paramonov.local_database_api.data.shared_pref.exception.NoParameterFoundException
import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository

class QuestionSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
): QuestionSettingsRepository {

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
        return try {
            val res = getString(DIFFICULTY_KEY)
            Difficulty.valueOf(res.toEnumName())
        } catch (ex: NoParameterFoundException) {
            getDefaultDifficulty()
        }
    }

    override fun saveDifficulty(difficulty: Difficulty) {
        saveString(DIFFICULTY_KEY, difficulty.name)
    }

    override fun getCategory(): Category {
        return try {
            val res = getString(CATEGORY_KEY)
            Category.valueOf(res.toEnumName())
        } catch (ex: NoParameterFoundException) {
            getDefaultCategory()
        }
    }

    override fun saveCategory(category: Category) {
        saveString(CATEGORY_KEY, category.name)
    }

    override fun getGameMode(): GameMode {
        return try {
            val res = getString(GAME_MODE_KEY)
            GameMode.valueOf(res.toEnumName())
        } catch (ex: NoParameterFoundException) {
            getDefaultGameMode()
        }
    }

    override fun saveGameMode(gameMode: GameMode) {
        saveString(GAME_MODE_KEY, gameMode.name)
    }

    override fun getLimit(): Int {
        val res = sharedPreferences.getInt(LIMIT_KEY, NO_LIMIT)
        return if (res == NO_LIMIT) getDefaultLimit()
        else res
    }

    override fun saveLimit(limit: Int) {
        sharedPreferences.edit().apply {
            putInt(LIMIT_KEY, limit)
            apply()
        }
    }

    private fun getDefaultDifficulty(): Difficulty = Difficulty.MEDIUM

    private fun getDefaultCategory(): Category = Category.GENERAL

    private fun getDefaultGameMode(): GameMode = GameMode.BLITZ

    private fun getDefaultLimit(): Int = DEFAULT_LIMIT

    companion object {
        private const val CATEGORY_KEY = "category"
        private const val DIFFICULTY_KEY = "difficulty"
        private const val GAME_MODE_KEY = "game_mode"
        private const val LIMIT_KEY = "limit"
        private const val DEFAULT_LIMIT = 50
        private const val NO_DATA = "NO_DATA"
        private const val NO_LIMIT = -1
    }
}