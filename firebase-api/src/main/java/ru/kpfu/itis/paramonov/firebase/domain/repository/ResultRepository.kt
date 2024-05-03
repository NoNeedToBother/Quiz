package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.firebase.domain.model.Result

interface ResultRepository {

    suspend fun get(gameMode: GameMode,
                    difficulty: Difficulty?,
                    category: Category?): List<Result>

    suspend fun save(result: Result): Double
}