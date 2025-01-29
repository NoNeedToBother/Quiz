package ru.kpfu.itis.paramonov.leaderboards.presentation.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.DifficultyUiModel

class DifficultyItem(
    val difficulty: DifficultyUiModel?,
) {
    val any: Boolean = (difficulty == null)

    override fun toString(): String {
        if (any) return "Any"
        return difficulty?.name?.normalizeEnumName() ?: "Any"
    }
}