package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel

class DifficultyItem(
    val difficulty: DifficultyUiModel
) {
    override fun toString(): String {
        return difficulty.name.normalizeEnumName()
    }
}