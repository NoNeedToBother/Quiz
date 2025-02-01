package ru.kpfu.itis.paramonov.leaderboards.api.usecase

import ru.kpfu.itis.paramonov.core.model.data.Difficulty

interface GetDifficultyUseCase {

    suspend operator fun invoke(): Difficulty
}