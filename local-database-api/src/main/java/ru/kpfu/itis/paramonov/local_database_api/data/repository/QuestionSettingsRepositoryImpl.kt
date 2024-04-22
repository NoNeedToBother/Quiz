package ru.kpfu.itis.paramonov.local_database_api.data.repository

import android.content.SharedPreferences
import ru.kpfu.itis.paramonov.common.utils.toEnumName
import ru.kpfu.itis.paramonov.local_database_api.data.exception.NoParameterFoundException
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Category
import ru.kpfu.itis.paramonov.local_database_api.domain.model.Difficulty
import ru.kpfu.itis.paramonov.local_database_api.domain.model.GameMode
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
            Difficulty.valueOf(res.uppercase())
        } catch (ex: NoParameterFoundException) {
            getDefaultDifficulty()
        }
    }

    override fun saveDifficulty(difficulty: String) {
        saveString(DIFFICULTY_KEY, difficulty.toEnumName())
    }

    override fun getCategory(): Category {
        return try {
            val res = getString(CATEGORY_KEY)
            Category.valueOf(res.uppercase())
        } catch (ex: NoParameterFoundException) {
            getDefaultCategory()
        }
    }

    override fun saveCategory(category: String) {
        saveString(CATEGORY_KEY, category.toEnumName())
    }

    override fun getGameMode(): GameMode {
        return try {
            val res = getString(GAME_MODE_KEY)
            GameMode.valueOf(res.uppercase())
        } catch (ex: NoParameterFoundException) {
            getDefaultGameMode()
        }
    }

    override fun saveGameMode(gameMode: String) {
        saveString(GAME_MODE_KEY, gameMode.toEnumName())
    }

    private fun getDefaultDifficulty(): Difficulty {
        return Difficulty.MEDIUM
    }

    private fun getDefaultCategory(): Category {
        return Category.GENERAL
    }

    private fun getDefaultGameMode(): GameMode {
        return GameMode.BLITZ
    }

    companion object {
        private const val CATEGORY_KEY = "category"
        private const val DIFFICULTY_KEY = "difficulty"
        private const val GAME_MODE_KEY = "game_mode"
        private const val NO_DATA = "NO_DATA"
    }
}