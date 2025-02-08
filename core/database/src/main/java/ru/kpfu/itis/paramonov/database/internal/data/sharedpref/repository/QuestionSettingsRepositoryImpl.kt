package ru.kpfu.itis.paramonov.database.internal.data.sharedpref.repository

import android.content.SharedPreferences
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.toEnumName
import ru.kpfu.itis.paramonov.database.R
import ru.kpfu.itis.paramonov.database.external.domain.exception.IncorrectParameterException
import ru.kpfu.itis.paramonov.database.external.domain.exception.NoParameterFoundException
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository

internal class QuestionSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val resourceManager: ResourceManager
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
        if (limit !in LIMIT_LOWER_BOUND..LIMIT_UPPER_BOUND)
            throw IncorrectParameterException(
                resourceManager.getString(R.string.incorrect_limit, LIMIT_LOWER_BOUND, LIMIT_UPPER_BOUND)
            )
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

        private const val LIMIT_LOWER_BOUND = 1
        private const val LIMIT_UPPER_BOUND = 100
    }
}
