package ru.kpfu.itis.paramonov.questions.api.usecase

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode

interface SaveQuestionSettingsUseCase {

    suspend operator fun invoke(
        difficulty: Difficulty?,
        category: Category?,
        gameMode: GameMode?)

}