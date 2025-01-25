package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel

class DifficultyItem(
    val difficulty: DifficultyUiModel
) {
    override fun toString(): String {
        return difficulty.name.normalizeEnumName()
    }
}