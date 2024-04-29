package ru.kpfu.itis.paramonov.firebase.domain.repository

import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.firebase.domain.model.Result

interface ResultRepository {

    suspend fun getByGameMode(gameMode: GameMode)

    suspend fun save(result: Result): Double
}